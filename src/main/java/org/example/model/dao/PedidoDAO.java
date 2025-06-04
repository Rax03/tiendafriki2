package org.example.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.model.entity.DetallesPedido;
import org.example.model.entity.Pedido;
import org.example.model.entity.Producto;
import org.example.model.entity.Usuario;
import org.example.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidoDAO {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // Obtener todos los pedidos junto con el usuario asociado y detalles cargados de forma Lazy.
    public List<Pedido> obtenerTodosLosPedidos() {
        EntityManager em = emf.createEntityManager();
        List<Pedido> pedidos = new ArrayList<>();
        try {
            // Se usa JOIN FETCH para cargar datos relacionados de usuario.
            pedidos = em.createQuery("SELECT p FROM Pedido p JOIN FETCH p.usuario", Pedido.class)
                    .getResultList();
            // Forzamos la carga de detalles (si están mapeados como LAZY)
            pedidos.forEach(p -> p.getDetallesPedidos().size());
        } catch (Exception e) {
            System.err.println("❌ Error al obtener pedidos: " + e.getMessage());
        } finally {
            em.close();
        }
        return pedidos;
    }

    // Obtener detalles del pedido específico, con el producto asociado cargado.
    public List<DetallesPedido> obtenerDetallesPedido(int idPedido) {
        EntityManager em = emf.createEntityManager();
        List<DetallesPedido> detalles = new ArrayList<>();
        try {
            detalles = em.createQuery(
                            "SELECT d FROM DetallesPedido d JOIN FETCH d.producto WHERE d.pedido.id = :id", DetallesPedido.class)
                    .setParameter("id", idPedido)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error al obtener detalles del pedido: " + e.getMessage());
        } finally {
            em.close();
        }
        return detalles;
    }

    // Registrar un pedido: se valida el usuario, se persiste el pedido, se crea cada detalle y se actualiza el stock del producto.
    public int registrarPedido(Pedido pedido, int idUsuario, List<Producto> carrito, Map<Integer, Integer> cantidadesSeleccionadas) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Buscar el usuario
            Usuario cliente = em.find(Usuario.class, idUsuario);
            if (cliente == null) {
                tx.rollback();
                throw new IllegalArgumentException("❌ Usuario no encontrado.");
            }

            pedido.setIdCliente(cliente);
            em.persist(pedido);

            // Procesar cada ítem del carrito
            for (Producto producto : carrito) {
                Producto p = em.find(Producto.class, producto.getId());
                int cantidad = cantidadesSeleccionadas.getOrDefault(p.getId(), 1);

                if (p.getStock() < cantidad) {
                    tx.rollback();
                    System.err.println("❌ Stock insuficiente para: " + p.getNombre());
                    return -1;
                }

                // Crear y persistir el detalle del pedido
                DetallesPedido detalle = new DetallesPedido(pedido, p, cantidad, p.getPrecio());
                em.persist(detalle);

                // Actualiza el stock del producto y realiza merge para reflejar los cambios.
                p.setStock(p.getStock() - cantidad);
                em.merge(p);
            }

            tx.commit();
            System.out.println("✅ Pedido registrado correctamente.");
            return pedido.getId();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Error en la transacción: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    // Actualizar un pedido existente.
    public boolean actualizarPedido(Pedido pedido) {
        return ejecutarTransaccion(em -> em.merge(pedido));
    }

    // Eliminar un pedido por ID.
    public boolean eliminarPedido(int idPedido) {
        return ejecutarTransaccion(em -> {
            Pedido pedido = em.find(Pedido.class, idPedido);
            if (pedido != null) {
                em.remove(pedido);
            }
        });
    }

    // Obtener un pedido por su ID
    public Pedido obtenerPedidoPorId(int idPedido) {
        EntityManager em = emf.createEntityManager();
        Pedido pedido = null;
        try {
            pedido = em.find(Pedido.class, idPedido);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener el pedido por ID: " + e.getMessage());
        } finally {
            em.close();
        }
        return pedido;
    }

    // Obtener los nombres de los productos asociados a un pedido
    public String obtenerProductosPorPedido(int idPedido) {
        EntityManager em = emf.createEntityManager();
        String productos = "";
        try {
            List<String> nombres = em.createQuery(
                            "SELECT d.producto.nombre FROM DetallesPedido d WHERE d.pedido.id = :id", String.class)
                    .setParameter("id", idPedido)
                    .getResultList();
            productos = String.join(", ", nombres);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener productos por pedido: " + e.getMessage());
        } finally {
            em.close();
        }
        return productos;
    }

    // Obtener el nombre del cliente asociado al pedido a partir del ID del cliente.
    public String obtenerNombreClientePorId(int idCliente) {
        EntityManager em = emf.createEntityManager();
        String nombreCliente = null;
        try {
            Usuario usuario = em.find(Usuario.class, idCliente);
            nombreCliente = (usuario != null) ? usuario.getNombre() : null;
        } catch (Exception e) {
            System.err.println("❌ Error al obtener nombre del cliente: " + e.getMessage());
        } finally {
            em.close();
        }
        return nombreCliente;
    }

    // Insertar un pedido (método auxiliar en caso de necesitarse de forma individual).
    public boolean insertarPedido(Pedido pedido) {
        return ejecutarTransaccion(em -> em.persist(pedido));
    }

    /**
     * Método utilitario para ejecutar transacciones y manejar el EntityManager.
     *
     * @param operacion Lambda que recibe un EntityManager y ejecuta una operación.
     * @return true si la transacción fue exitosa; false en caso de error.
     */
    private boolean ejecutarTransaccion(TransaccionLambda operacion) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            operacion.ejecutar(em);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Error en la transacción: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Interfaz funcional para operaciones en transacciones.
    @FunctionalInterface
    private interface TransaccionLambda {
        void ejecutar(EntityManager em);
    }
}
