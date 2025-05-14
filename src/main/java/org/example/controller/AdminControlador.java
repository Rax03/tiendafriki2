package org.example.controller;

import org.example.model.dao.UsuarioDAO;
import org.example.model.service.LoginService;
import org.example.model.service.UsuarioService;
import org.example.view.*;

import javax.swing.*;

public class AdminControlador {
    private final AdminVista vista;

    public AdminControlador(AdminVista vista) {
        this.vista = vista;
        // Vincular los eventos de los botones
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnProductos().addActionListener(e -> abrirVista(ProductoVista.class));
        vista.getBtnProveedores().addActionListener(e -> abrirVista(ProveedorVista.class));
        vista.getBtnCategorias().addActionListener(e -> abrirVista(CategoriaVista.class));
        vista.getBtnPedidos().addActionListener(e -> abrirVista(PedidoVista.class));
        vista.getBtnUsuarios().addActionListener(e -> abrirVista(UsuariosVista.class));
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    // Método genérico para abrir vistas
    private void abrirVista(Class<? extends JFrame> vistaClass) {
        try {
            System.out.println("Botón " + vistaClass.getSimpleName() + " presionado."); // Depuración
            // Se crea la vista y se muestra
            JFrame vistaInstance = vistaClass.getDeclaredConstructor().newInstance();
            vistaInstance.setVisible(true);
        } catch (Exception e) {
            mostrarError("Abrir vista " + vistaClass.getSimpleName(), e);
        }
    }

    private void cerrarSesion() {
        try {
            System.out.println("Botón Cerrar Sesión presionado."); // Depuración
            int confirmacion = JOptionPane.showConfirmDialog(vista,
                    "¿Estás seguro de que deseas cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Cerrar la ventana de AdminVista y abrir LoginVista
                vista.dispose();
                LoginVista loginVista = new LoginVista();
                // Se reutiliza el controlador de Login
                LoginService loginService = new LoginService( new UsuarioService(new UsuarioDAO()));
                new LoginControlador(loginVista, loginService);
                loginVista.setVisible(true);
            }
        } catch (Exception e) {
            mostrarError("Cerrar Sesión", e);
        }
    }

    private void mostrarError(String modulo, Exception e) {
        JOptionPane.showMessageDialog(vista, "Error al realizar la acción de " + modulo + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
