package org.example.controller;

import org.example.model.dao.UsuarioDAO;
import org.example.model.entity.Enum.Rol;
import org.example.model.entity.Usuario;
import org.example.model.service.LoginService;
import org.example.model.service.UsuarioService;
import org.example.utils.HashUtil;
import org.example.view.LoginVista;
import org.example.view.RegistroUsuarioVista;

import javax.swing.*;
import java.time.LocalDate;

public class RegistroUsuarioControlador {
    private final RegistroUsuarioVista vista;
    private final UsuarioService usuarioService;

    public RegistroUsuarioControlador(RegistroUsuarioVista vista) {
        this.vista = vista;
        this.usuarioService = new UsuarioService(new UsuarioDAO());

        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBotonRegistrar().addActionListener(e -> registrarUsuario());
        vista.getBotonCancelar().addActionListener(e -> cancelarRegistro());
    }

    private void registrarUsuario() {
        String nombre = vista.getNombre();
        String email = vista.getEmail();
        String contraseña = vista.getContraseña();
        String confirmarContraseña = vista.getConfirmarContraseña();
        String rolSeleccionado = vista.getRolSeleccionado();

        if (!validarCampos(nombre, email, contraseña, confirmarContraseña)) {
            return;
        }

        if (!HashUtil.validarEmail(email)) {
            JOptionPane.showMessageDialog(vista, "El correo no tiene un formato válido.");
            return;
        }

        if (!contraseña.equals(confirmarContraseña)) {
            JOptionPane.showMessageDialog(vista, "Las contraseñas no coinciden.");
            return;
        }

        if (usuarioService.correoExiste(email)) {
            JOptionPane.showMessageDialog(vista, "El correo ya está registrado.");
            return;
        }

        // Validar y asignar rol
        Rol rol = obtenerRol(rolSeleccionado);
        if (rol == null) {
            JOptionPane.showMessageDialog(vista, "Rol no válido.");
            return;
        }

        Usuario usuario = new Usuario(0, nombre, email, contraseña, null, rol, LocalDate.now());

        if (usuarioService.registrarUsuario(usuario)) {
            JOptionPane.showMessageDialog(vista, "Usuario registrado exitosamente.");
            vista.dispose();
            abrirInicioSesion();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar usuario.");
        }
    }


    private boolean validarCampos(String nombre, String email, String contraseña, String confirmarContraseña) {
        if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }

    private Rol obtenerRol(String rolSeleccionado) {
        try {
            return Rol.valueOf(rolSeleccionado);
        } catch (IllegalArgumentException e) {
            return null; // Rol no válido
        }
    }

    private void cancelarRegistro() {
        vista.dispose();
        abrirInicioSesion();
    }

    private void abrirInicioSesion() {
        LoginVista loginVista = new LoginVista();
        LoginService loginService = new LoginService(new UsuarioService(new UsuarioDAO()));
        new LoginControlador(loginVista, loginService);  // Llamar al constructor con los dos parámetros
        loginVista.setVisible(true);
    }
}
