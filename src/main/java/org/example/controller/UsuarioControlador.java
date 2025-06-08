package org.example.controller;

import org.example.model.dao.PedidoDAO;
import org.example.model.dao.ProductoDAO;
import org.example.model.dao.UsuarioDAO;
import org.example.model.entity.Pedido;
import org.example.model.entity.Producto;
import org.example.model.entity.Usuario;
import org.example.view.UsuarioVista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class UsuarioControlador {
    private final UsuarioVista vista;
    private final PedidoDAO pedidoDAO;
    private final ProductoDAO productoDAO;
    private final int idUsuario;
    private final List<Producto> carrito;
    private final Map<Integer, Integer> cantidadesSeleccionadas;
    private List<Producto> listaProductos;

    public UsuarioControlador(UsuarioVista vista, int idUsuario) {
        this.vista = vista;
        this.pedidoDAO = new PedidoDAO();
        this.productoDAO = new ProductoDAO();
        this.idUsuario = idUsuario;
        this.carrito = new ArrayList<>();
        this.cantidadesSeleccionadas = new HashMap<>();
        inicializarEventos();
        cargarProductosEnTabla();
    }

    private void inicializarEventos() {
        vista.getBotonAgregarCarrito().addActionListener(e -> agregarProductoAlCarrito());
        vista.getBotonEliminarCarrito().addActionListener(e -> eliminarProductoDelCarrito());
        vista.getBotonVerCarrito().addActionListener(e -> mostrarCarrito());
        vista.getBotonFinalizarCompra().addActionListener(e -> finalizarCompra());
        vista.getBotonBuscar().addActionListener(e -> cargarProductosEnTabla());
    }

    private void cargarProductosEnTabla() {
        String filtro = vista.getCampoBusqueda().getText().trim().toLowerCase();
        listaProductos = productoDAO.obtenerTodosLosProductos();

        // Ordenar los productos alfabéticamente por nombre
        listaProductos.sort(Comparator.comparing(Producto::getNombre));

        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);

        for (Producto p : listaProductos) {
            if (filtro.isEmpty() || p.getNombre().toLowerCase().contains(filtro)) {
                ImageIcon icono = null;
                try {
                    Image img = new ImageIcon(p.getImagen()).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    icono = new ImageIcon(img);
                } catch (Exception ignored) {
                }
                modelo.addRow(new Object[]{
                        icono,
                        p.getNombre(),
                        String.format("$%.2f", p.getPrecio()), // Formato correcto con dos decimales
                        p.getStock() > 0 ? p.getStock() + " unidades" : "Sin stock"
                });
            }
        }
    }

    private void agregarProductoAlCarrito() {
        int filaSeleccionada = vista.getTablaProductos().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto.");
            return;
        }

        String nombreProducto = (String) vista.getModeloTabla().getValueAt(filaSeleccionada, 1);
        Producto productoSeleccionado = listaProductos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreProducto))
                .findFirst()
                .orElse(null);

        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Producto no encontrado.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(vista.getCampoCantidad().getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "❌ Ingresa una cantidad válida.");
            return;
        }

        if (cantidad <= 0 || cantidad > productoSeleccionado.getStock()) {
            JOptionPane.showMessageDialog(vista, "❌ Cantidad no disponible en stock.");
            return;
        }

        if (!carrito.contains(productoSeleccionado)) {
            carrito.add(productoSeleccionado);
        }

        cantidadesSeleccionadas.put(productoSeleccionado.getId(), cantidad);

        JOptionPane.showMessageDialog(vista, "✅ Producto agregado al carrito: " + productoSeleccionado.getNombre() + " x " + cantidad);
        mostrarCarrito();
    }

    private void eliminarProductoDelCarrito() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El carrito está vacío.");
            return;
        }

        String productoAEliminar = JOptionPane.showInputDialog(vista, "Ingresa el nombre del producto a eliminar:");
        if (productoAEliminar == null || productoAEliminar.trim().isEmpty()) return;

        Producto productoEncontrado = carrito.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(productoAEliminar.trim()))
                .findFirst()
                .orElse(null);

        if (productoEncontrado != null) {
            carrito.remove(productoEncontrado);
            cantidadesSeleccionadas.remove(productoEncontrado.getId());
            mostrarCarrito();
            JOptionPane.showMessageDialog(vista, "Producto eliminado del carrito.");
        } else {
            JOptionPane.showMessageDialog(vista, "Producto no encontrado en el carrito.");
        }
    }

    private void mostrarCarrito() {
        StringBuilder texto = new StringBuilder();
        for (Producto p : carrito) {
            int cantidad = cantidadesSeleccionadas.getOrDefault(p.getId(), 1);
            BigDecimal subtotal = p.getPrecio().multiply(BigDecimal.valueOf(cantidad)); // No necesitas conversión

            texto.append(cantidad)
                    .append(" x ")
                    .append(p.getNombre())
                    .append(" = $")
                    .append(String.format("%.2f", subtotal)) // Formato garantizado con dos decimales
                    .append("\n");
        }
        vista.getAreaCarrito().setText(texto.toString());
    }


    private void finalizarCompra() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El carrito está vacío.");
            return;
        }

        // Pedir dirección de envío
        String direccionEnvio = JOptionPane.showInputDialog(vista, "Ingrese su dirección de envío:");
        if (direccionEnvio == null || direccionEnvio.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debe ingresar una dirección de envío válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular total
        BigDecimal total = BigDecimal.ZERO;
        for (Producto p : carrito) {
            int cantidad = cantidadesSeleccionadas.getOrDefault(p.getId(), 1);
            total = total.add(p.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
        }

        // Obtener el usuario antes de crear el pedido
        Usuario usuario = new UsuarioDAO().obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            JOptionPane.showMessageDialog(vista, "❌ Error: Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear pedido con el usuario asignado
        Pedido pedido = new Pedido();
        pedido.setEstado("Pendiente");
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(total);
        pedido.setIdUsuario(usuario);  // ✅ Ahora el usuario está correctamente asignado
        pedido.setDireccionEnvio(direccionEnvio);

        int idPedido = pedidoDAO.registrarPedido(pedido, idUsuario, carrito, cantidadesSeleccionadas);

        if (idPedido > 0) {
            JOptionPane.showMessageDialog(vista, "✅ Compra realizada con éxito. ID Pedido: " + idPedido + "\nDirección: " + direccionEnvio);
            carrito.clear();
            cantidadesSeleccionadas.clear();
            vista.getAreaCarrito().setText("");
            cargarProductosEnTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "❌ Error al registrar la compra.");
        }
    }

}
