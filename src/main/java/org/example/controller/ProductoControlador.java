package org.example.controller;

import org.example.model.dao.ProductosDAO;
import org.example.model.entity.Producto;
import org.example.view.ProductoVista;

import javax.swing.*;
import java.util.Optional;

public class ProductoControlador {

    private final ProductoVista productoVista;
    private final ProductosDAO productosDAO;

    public ProductoControlador() {
        // Inicialización de la vista y el DAO
        productoVista = new ProductoVista();
        productosDAO = new ProductosDAO();

        // Conectar acciones de la vista con el controlador
        conectarAcciones();
    }

    private void conectarAcciones() {
        // Configurar la acción de "Agregar Producto"
        productoVista.getBtnAgregar().addActionListener(e -> mostrarFormularioAgregar());

        // Configurar la acción de "Editar Producto"
        productoVista.getBtnEditar().addActionListener(e -> mostrarFormularioEditar());

        // Configurar la acción de "Eliminar Producto"
        productoVista.getBtnEliminar().addActionListener(e -> eliminarProducto());
    }

    private void mostrarFormularioAgregar() {
        try {
            productoVista.mostrarFormularioAgregar();
            productoVista.llenarTablaProductos(); // Actualizar la tabla después de la acción
        } catch (Exception ex) {
            mostrarError("Error al agregar producto: " + ex.getMessage());
        }
    }

    private void mostrarFormularioEditar() {
        // Verificar si hay un producto seleccionado
        Optional<Producto> productoSeleccionado = Optional.ofNullable(productoVista.getProductoSeleccionado());
        if (productoSeleccionado.isEmpty()) {
            mostrarAdvertencia("Debes seleccionar un producto para editar.");
            return;
        }

        try {
            productoVista.mostrarFormularioEditar();
            productoVista.llenarTablaProductos(); // Actualizar tabla después de la edición
        } catch (Exception ex) {
            mostrarError("Error al editar producto: " + ex.getMessage());
        }
    }

    private void eliminarProducto() {
        // Verificar si hay un producto seleccionado
        Optional<Producto> productoSeleccionado = Optional.ofNullable(productoVista.getProductoSeleccionado());
        if (productoSeleccionado.isEmpty()) {
            mostrarAdvertencia("Debes seleccionar un producto para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                productoVista,
                "¿Estás seguro de que quieres eliminar este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                productosDAO.eliminarProducto(productoSeleccionado.get().getId_producto());
                productoVista.llenarTablaProductos(); // Actualizar la tabla después de la eliminación
                mostrarMensaje("Producto eliminado correctamente.");
            } catch (Exception ex) {
                mostrarError("Error al eliminar producto: " + ex.getMessage());
            }
        }
    }

    // Métodos auxiliares para mostrar mensajes
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(productoVista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(productoVista, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(productoVista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
