package org.example.view;

import org.example.model.dao.UsuarioDAO;
import org.example.model.entity.Usuario;

import javax.swing.*;
import java.awt.*;

public class PerfilVista extends JFrame {
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtContraseña;

    public PerfilVista(Usuario usuario) {
        setTitle("Editar Perfil");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Título en la parte superior
        JLabel lblTitulo = new JLabel("Editar Perfil", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        // Formulario de edición
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        formulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(usuario.getNombre()); // Cargar el nombre desde el objeto usuario
        formulario.add(txtNombre);

        formulario.add(new JLabel("Email:"));
        txtEmail = new JTextField(usuario.getEmail()); // Cargar el email desde el objeto usuario
        formulario.add(txtEmail);

        formulario.add(new JLabel("Contraseña:"));
        txtContraseña = new JPasswordField(usuario.getPassword()); // Cargar la contraseña desde el objeto usuario (aunque idealmente debe estar encriptada)
        formulario.add(txtContraseña);

        add(formulario, BorderLayout.CENTER);

        // Botón para guardar cambios
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(e -> guardarCambios(usuario));
        add(btnGuardar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void guardarCambios(Usuario usuario) {
        // Actualizar el objeto Usuario con los datos del formulario
        usuario.setNombre(txtNombre.getText());
        usuario.setEmail(txtEmail.getText());
        usuario.setPassword(new String(txtContraseña.getPassword()));


         UsuarioDAO usuarioDAO = new UsuarioDAO();
         usuarioDAO.actualizarUsuario(usuario);


        JOptionPane.showMessageDialog(this, "Cambios guardados exitosamente.");
    }
}
