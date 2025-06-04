package org.example.controller;

import org.example.model.dao.ProductoDAO;
import org.example.model.entity.Producto;
import org.example.view.ProductoVista;

import javax.swing.*;
import java.util.List;

public class ProductoControlador {

    private final ProductoVista productoVista;
    private final ProductoDAO productoDAO;

    public ProductoControlador() {
        productoDAO = new ProductoDAO();
        productoVista = new ProductoVista();
        configurarAcciones();
        refrescarTabla();
        productoVista.setVisible(true);
    }

    private void configurarAcciones() {
        // Acción para agregar producto.
        productoVista.getBtnAgregar().addActionListener(e -> {
            productoVista.mostrarFormularioAgregar();
            // Aquí se debería persistir el nuevo producto. Después de guardar se actualiza la tabla.
            refrescarTabla();
        });

        // Acción para editar un producto.
        productoVista.getBtnEditar().addActionListener(e -> {
            if (productoVista.getProductoSeleccionado() != null) {
                productoVista.mostrarFormularioEditar();
                refrescarTabla();
            } else {
                JOptionPane.showMessageDialog(productoVista, "Seleccione un producto para editar.");
            }
        });

        // Acción para eliminar un producto.
        productoVista.getBtnEliminar().addActionListener(e -> {
            Producto productoSeleccionado = productoVista.getProductoSeleccionado();
            if (productoSeleccionado != null) {
                int confirmacion = JOptionPane.showConfirmDialog(productoVista,
                        "¿Está seguro de eliminar el producto?", "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean exito = productoDAO.eliminarProducto(productoSeleccionado.getId());
                    if (exito) {
                        JOptionPane.showMessageDialog(productoVista, "Producto eliminado exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(productoVista, "Error al eliminar producto.");
                    }
                    refrescarTabla();
                }
            } else {
                JOptionPane.showMessageDialog(productoVista, "Seleccione un producto para eliminar.");
            }
        });
    }

    private void refrescarTabla() {
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        productoVista.llenarTablaProductos(productos);
    }
}
