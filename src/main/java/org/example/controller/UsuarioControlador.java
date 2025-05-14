package org.example.controller;

import org.example.model.dao.PedidoDAO;
import org.example.model.dao.ProductosDAO;
import org.example.model.entity.Pedido;
import org.example.model.entity.Producto;
import org.example.view.UsuarioVista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioControlador {
    private final UsuarioVista vista;
    private final PedidoDAO pedidoDAO;
    private final ProductosDAO productoDAO;
    private final int idUsuario;
    private final List<Producto> carrito;
    private final Map<Integer, Integer> cantidadesSeleccionadas; // ✅ Almacena cantidades por producto
    private List<Producto> listaProductos;

    public UsuarioControlador(UsuarioVista vista, int idUsuario) {
        this.vista = vista;
        this.pedidoDAO = new PedidoDAO();
        this.productoDAO = new ProductosDAO();
        this.idUsuario = idUsuario;
        this.carrito = new ArrayList<>();
        this.cantidadesSeleccionadas = new HashMap<>(); // ✅ Inicializar mapa
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
        String filtro = vista.getCampoBusqueda().getText().trim().toLowerCase(); // ✅ Obtener el texto del campo de búsqueda
        listaProductos = productoDAO.obtenerTodosLosProductos();
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);

        for (Producto p : listaProductos) {
            // ✅ Filtrar por nombre si el filtro no está vacío
            if (filtro.isEmpty() || p.getNombre().toLowerCase().contains(filtro)) {
                ImageIcon icono;
                try {
                    icono = new ImageIcon(p.getImagen());
                    Image img = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    icono = new ImageIcon(img);
                } catch (Exception e) {
                    icono = new ImageIcon();
                }
                modelo.addRow(new Object[]{
                        icono,
                        p.getNombre(),
                        p.getPrecio(),
                        p.getStock()
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

        Producto productoSeleccionado = listaProductos.get(filaSeleccionada);

        int cantidad;
        try {
            cantidad = Integer.parseInt(vista.getCampoCantidad().getText()); // ✅ Obtener cantidad ingresada por el usuario
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "❌ Ingresa una cantidad válida.");
            return;
        }

        if (cantidad > productoSeleccionado.getStock() || cantidad <= 0) {
            JOptionPane.showMessageDialog(vista, "❌ Cantidad no disponible en stock.");
            return;
        }

        // ✅ Guardamos el producto en el carrito SIN modificar la lista original
        carrito.add(productoSeleccionado);

        // ✅ Guardamos la cantidad en el mapa de cantidades
        cantidadesSeleccionadas.put(productoSeleccionado.getId_producto(), cantidad);

        JOptionPane.showMessageDialog(vista, "✅ Producto agregado al carrito: " + productoSeleccionado.getNombre() + " x " + cantidad);
        mostrarCarrito();
    }


    private void eliminarProductoDelCarrito() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El carrito está vacío.");
            return;
        }

        String productoAEliminar = JOptionPane.showInputDialog(vista, "Ingresa el nombre del producto a eliminar:");
        carrito.removeIf(p -> p.getNombre().equalsIgnoreCase(productoAEliminar));
        cantidadesSeleccionadas.remove(productoAEliminar); // ✅ También eliminar la cantidad guardada
        mostrarCarrito();
    }

    private void mostrarCarrito() {
        StringBuilder texto = new StringBuilder();
        for (Producto p : carrito) {
            int cantidadSeleccionada = cantidadesSeleccionadas.getOrDefault(p.getId_producto(), 1); // ✅ Obtener cantidad del mapa
            texto.append(cantidadSeleccionada)
                    .append(" x ")
                    .append(p.getNombre())
                    .append(" = $")
                    .append(p.getPrecio() * cantidadSeleccionada)
                    .append("\n");
        }
        vista.getAreaCarrito().setText(texto.toString());
    }

    private void finalizarCompra() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El carrito está vacío.");
            return;
        }

        float total = 0;
        for (Producto p : carrito) {
            total += p.getPrecio() * cantidadesSeleccionadas.getOrDefault(p.getId_producto(), 1); // ✅ Usar cantidad correcta
        }

        Pedido pedido = new Pedido();
        pedido.setEstado("Pendiente");
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(total);
        pedido.setIdCliente(idUsuario);

        int idPedido = pedidoDAO.registrarPedido(pedido, idUsuario, carrito, cantidadesSeleccionadas); // ✅ Ahora enviamos el mapa

        if (idPedido > 0) {
            JOptionPane.showMessageDialog(vista, "Compra realizada con éxito. ID Pedido: " + idPedido);
            carrito.clear();
            cantidadesSeleccionadas.clear(); // ✅ Vaciar mapa después de compra
            vista.getAreaCarrito().setText("");
            cargarProductosEnTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar la compra.");
        }
    }
}
