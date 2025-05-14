package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginVista extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoContraseña;
    private JButton botonLogin, botonRegistrar;

    public LoginVista() {
        setTitle("Inicio de Sesión - Tienda Friki");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear los componentes
        JLabel etiquetaTitulo = new JLabel("¡Bienvenido a Tienda Friki!");
        etiquetaTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        etiquetaTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaTitulo.setForeground(new Color(128, 0, 128));

        JLabel etiquetaEmail = new JLabel("Correo Electrónico:");
        etiquetaEmail.setForeground(new Color(0, 102, 204));

        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        etiquetaContraseña.setForeground(new Color(0, 102, 204));

        campoEmail = new JTextField(20);
        campoContraseña = new JPasswordField(20);

        botonLogin = new JButton("Iniciar Sesión");
        botonLogin.setBackground(new Color(0, 204, 102));
        botonLogin.setForeground(Color.WHITE);
        botonLogin.setFocusPainted(false);

        botonRegistrar = new JButton("Registrarse");
        botonRegistrar.setBackground(new Color(255, 153, 0));
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setFocusPainted(false);

        // Configuración del panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240));
        panel.setLayout(null);

        // Posicionar componentes
        etiquetaTitulo.setBounds(50, 20, 300, 30);
        etiquetaEmail.setBounds(50, 80, 150, 20);
        campoEmail.setBounds(180, 80, 150, 25);
        etiquetaContraseña.setBounds(50, 120, 150, 20);
        campoContraseña.setBounds(180, 120, 150, 25);
        botonLogin.setBounds(50, 180, 130, 30);
        botonRegistrar.setBounds(200, 180, 130, 30);

        // Agregar componentes al panel
        panel.add(etiquetaTitulo);
        panel.add(etiquetaEmail);
        panel.add(campoEmail);
        panel.add(etiquetaContraseña);
        panel.add(campoContraseña);
        panel.add(botonLogin);
        panel.add(botonRegistrar);

        // Agregar el panel a la ventana
        add(panel);
    }

    // Obtener los datos de los campos
    public String getEmail() {
        return campoEmail.getText();
    }

    public String getContraseña() {
        return new String(campoContraseña.getPassword());
    }

    // Métodos para obtener los botones
    public JButton getBotonLogin() {
        return botonLogin;
    }

    public JButton getBotonRegistrar() {
        return botonRegistrar;
    }

    // Método para agregar los action listeners a los botones
    public void agregarActionListener(ActionListener listenerLogin, ActionListener listenerRegistrar) {
        botonLogin.addActionListener(listenerLogin);
        botonRegistrar.addActionListener(listenerRegistrar);
    }
}
