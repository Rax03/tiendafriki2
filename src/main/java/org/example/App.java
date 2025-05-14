package org.example;

import org.example.controller.LoginControlador;
import org.example.model.dao.UsuarioDAO;
import org.example.model.service.LoginService;
import org.example.model.service.UsuarioService;
import org.example.view.LoginVista;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var loginVista = new LoginVista();
            var loginService = new LoginService(new UsuarioService(new UsuarioDAO()));
            new LoginControlador(loginVista, loginService);
            loginVista.setVisible(true);
        });
    }
}
