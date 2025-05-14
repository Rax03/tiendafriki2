package org.example.view;

import javax.swing.*;
import java.awt.*;

public class RegistroUsuarioVista extends JFrame {
    private JTextField campoNombre;
    private JTextField campoEmail;
    private JPasswordField campoContraseña;
    private JPasswordField campoConfirmarContraseña;
    private JComboBox<String> comboRol;
    private JButton botonRegistrar;
    private JButton botonCancelar;

    public RegistroUsuarioVista() {
        configurarVentana();

        JPanel panelSuperior = crearPanelSuperior();
        JPanel panelPrincipal = crearPanelPrincipal();
        JPanel panelInferior = crearPanelInferior();

        add(panelSuperior, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // ✅ ¡No llamamos a setVisible aquí!
    }

    private void configurarVentana() {
        setTitle("Registro de Usuario");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel crearPanelSuperior() {
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(52, 58, 64));
        JLabel lblTitulo = new JLabel("Registro de Usuario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(255, 204, 0));
        panelSuperior.add(lblTitulo);
        return panelSuperior;
    }

    private JPanel crearPanelPrincipal() {
        JPanel panelPrincipal = new JPanel(new GridLayout(5, 2, 10, 10));
        panelPrincipal.setBackground(new Color(28, 28, 30));

        campoNombre = new JTextField(20);
        campoEmail = new JTextField(20);
        campoContraseña = new JPasswordField(20);
        campoConfirmarContraseña = new JPasswordField(20);
        comboRol = new JComboBox<>(new String[]{"ADMIN", "CLIENTE"});

        agregarCampo(panelPrincipal, "Nombre:", campoNombre);
        agregarCampo(panelPrincipal, "Email:", campoEmail);
        agregarCampo(panelPrincipal, "Contraseña:", campoContraseña);
        agregarCampo(panelPrincipal, "Confirmar Contraseña:", campoConfirmarContraseña);
        agregarCampo(panelPrincipal, "Rol:", comboRol);

        return panelPrincipal;
    }

    private JPanel crearPanelInferior() {
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(52, 58, 64));

        botonRegistrar = new JButton("Registrar");
        botonCancelar = new JButton("Cancelar");

        panelInferior.add(botonRegistrar);
        panelInferior.add(botonCancelar);

        return panelInferior;
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        JLabel label = new JLabel(etiqueta);
        label.setForeground(Color.WHITE);
        panel.add(label);
        panel.add(campo);
    }

    public JButton getBotonRegistrar() {
        return botonRegistrar;
    }

    public JButton getBotonCancelar() {
        return botonCancelar;
    }

    public String getNombre() {
        return campoNombre.getText();
    }

    public String getEmail() {
        return campoEmail.getText();
    }

    public String getContraseña() {
        return new String(campoContraseña.getPassword());
    }

    public String getConfirmarContraseña() {
        return new String(campoConfirmarContraseña.getPassword());
    }

    public String getRolSeleccionado() {
        return comboRol.getSelectedItem().toString().toUpperCase();
    }
}
