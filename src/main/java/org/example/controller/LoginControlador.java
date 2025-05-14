package org.example.controller;

import org.example.model.entity.Usuario;
import org.example.model.service.LoginService;
import org.example.view.AdminVista;
import org.example.view.LoginVista;
import org.example.view.RegistroUsuarioVista;
import org.example.view.UsuarioVista;

import javax.swing.*;

public class LoginControlador {
    private final LoginVista vista;
    private final LoginService loginService;

    public LoginControlador(LoginVista vista, LoginService loginService) {
        this.vista = vista;
        this.loginService = loginService;

        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBotonLogin().addActionListener(e -> autenticarUsuario());
        vista.getBotonRegistrar().addActionListener(e -> abrirRegistroUsuario());
    }

    private void autenticarUsuario() {
        String email = vista.getEmail();
        String contraseña = vista.getContraseña();

        if (email.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debe ingresar el correo y la contraseña.");
            return;
        }

        Usuario usuario = loginService.autenticarUsuario(email, contraseña);
        if (usuario != null) {
            JOptionPane.showMessageDialog(vista, "Inicio de sesión exitoso. Bienvenido " + usuario.getNombre());

            vista.dispose();

            switch (usuario.getRol()) {
                case ADMIN:
                    AdminVista adminVista = new AdminVista();
                    new AdminControlador(adminVista);
                    adminVista.setVisible(true);
                    break;
                case CLIENTE:
                    UsuarioVista usuarioVista = new UsuarioVista();
                    new UsuarioControlador(usuarioVista , usuario.getId());
                    usuarioVista.setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Rol no reconocido.");
            }

        } else {
            JOptionPane.showMessageDialog(vista, "Correo o contraseña incorrectos.");
        }
    }

    private void abrirRegistroUsuario() {
        vista.dispose();
        RegistroUsuarioVista registroVista = new RegistroUsuarioVista();
        registroVista.setVisible(true);
        new RegistroUsuarioControlador(registroVista);
    }

}
