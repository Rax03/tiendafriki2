package org.example.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.model.entity.Producto;
import org.example.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // Método privado para obtener un EntityManager
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Agregar un nuevo producto
    public boolean agregarProducto(Producto producto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(producto);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("❌ Error al agregar producto: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Actualizar un producto existente
    public boolean actualizarProducto(Producto producto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(producto);
            tx.commit();
            return true;
        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Eliminar un producto por su ID
    public boolean eliminarProducto(Integer idProducto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Producto producto = em.find(Producto.class, idProducto);
            if (producto == null) {
                System.err.println("❌ Producto no encontrado con id: " + idProducto);
                return false;
            }
            tx.begin();
            em.remove(producto);
            tx.commit();
            return true;
        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Obtener un producto por su ID
    public Producto obtenerProductoPorId(Integer idProducto) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, idProducto);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener producto por id: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    // Obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {
        EntityManager em = getEntityManager();
        List<Producto> productos = new ArrayList<>();
        try {
            TypedQuery<Producto> query = em.createQuery("SELECT p FROM Producto p", Producto.class);
            productos = query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error al obtener todos los productos: " + e.getMessage());
        } finally {
            em.close();
        }
        return productos;
    }

    // Obtener productos junto con su categoría (LEFT JOIN FETCH) para evitar LAZY loading
    public List<Producto> obtenerProductosConCategoria() {
        EntityManager em = getEntityManager();
        List<Producto> productos = new ArrayList<>();
        try {
            TypedQuery<Producto> query = em.createQuery(
                    "SELECT p FROM Producto p JOIN FETCH p.idCategoria", Producto.class);
            productos = query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error al obtener productos con categoría: " + e.getMessage());
        } finally {
            em.close();
        }
        return productos;
    }

    // Verificar si existe un producto por su ID
    public boolean existeProducto(Integer idProducto) {
        return obtenerProductoPorId(idProducto) != null;
    }
}
