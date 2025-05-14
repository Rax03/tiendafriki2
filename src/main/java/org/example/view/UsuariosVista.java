package org.example.view;

import org.example.model.dao.UsuarioDAO;
import org.example.model.entity.Enum.Rol;
import org.example.model.entity.Usuario;
import org.example.utils.HashUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuariosVista extends JFrame {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private UsuarioDAO usuarioDAO;

    public UsuariosVista() {
        setTitle("Gestión de Usuarios");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        usuarioDAO = new UsuarioDAO();

        // Configuración del panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Usuarios", JLabel.CENTER);
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        panelPrincipal.setBackground(new Color(34, 34, 34));

        // Configurar la tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Email", "Rol", "Fecha Registro"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.setGridColor(Color.GRAY);
        tablaUsuarios.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tablaUsuarios.getTableHeader().setBackground(new Color(52, 58, 64));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);

        // Configurar botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar Usuario");
        JButton btnEditar = new JButton("Editar Usuario");
        JButton btnEliminar = new JButton("Eliminar Usuario");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Llenar la tabla con los datos de los usuarios
        llenarTablaUsuarios();

        // Agregar componentes al panel principal
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Agregar panel principal al JFrame
        add(panelPrincipal);
        setVisible(true);
    }

    private void llenarTablaUsuarios() {
        try {
            modeloTabla.setRowCount(0); // Limpiar la tabla antes de llenarla
            List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
            for (Usuario usuario : usuarios) {
                modeloTabla.addRow(new Object[]{
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getRol().name(),
                        usuario.getFechaRegistro()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void mostrarFormularioAgregar() {
        JTextField txtNombre = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtContraseña = new JPasswordField();
        JComboBox<Rol> cmbRol = new JComboBox<>(Rol.values());

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Email:"));
        panelFormulario.add(txtEmail);
        panelFormulario.add(new JLabel("Contraseña:"));
        panelFormulario.add(txtContraseña);
        panelFormulario.add(new JLabel("Rol:"));
        panelFormulario.add(cmbRol);

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Agregar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText().trim();
                String email = txtEmail.getText().trim();
                String contraseña = new String(txtContraseña.getPassword());

                // ✅ Validación de campos antes de registrar el usuario
                if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "❌ Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!HashUtil.validarEmail(email)) {
                    JOptionPane.showMessageDialog(this, "❌ Email no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ✅ Hashear la contraseña con salt
                String salt = HashUtil.generarSalt();
                String contraseñaHasheada = HashUtil.hashearConSalt(contraseña, salt);

                Usuario usuario = new Usuario(
                        0,
                        nombre,
                        email,
                        contraseñaHasheada, // ✅ Guardar la contraseña hasheada
                        salt,
                        (Rol) cmbRol.getSelectedItem(),
                        java.time.LocalDate.now()
                );

                if (usuarioDAO.registrarUsuario(usuario)) {
                    JOptionPane.showMessageDialog(this, "✅ Usuario registrado exitosamente con contraseña segura.");
                    llenarTablaUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Error en los datos ingresados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void mostrarFormularioEditar() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para editar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        Usuario usuarioExistente = usuarioDAO.obtenerDatosUsuario(idUsuario);

        if (usuarioExistente == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField txtNombre = new JTextField(usuarioExistente.getNombre());
        JTextField txtEmail = new JTextField(usuarioExistente.getEmail());
        JPasswordField txtContraseña = new JPasswordField(); // Campo vacío para contraseñas
        JComboBox<Rol> cmbRol = new JComboBox<>(Rol.values());
        cmbRol.setSelectedItem(usuarioExistente.getRol());

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Email:"));
        panelFormulario.add(txtEmail);
        panelFormulario.add(new JLabel("Contraseña (opcional):"));
        panelFormulario.add(txtContraseña);
        panelFormulario.add(new JLabel("Rol:"));
        panelFormulario.add(cmbRol);

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                usuarioExistente.setNombre(txtNombre.getText());
                usuarioExistente.setEmail(txtEmail.getText());
                String nuevaContraseña = new String(txtContraseña.getPassword());
                if (!nuevaContraseña.isEmpty()) {
                    usuarioExistente.setPassword(nuevaContraseña); // Actualizar solo si se ingresó algo
                }
                usuarioExistente.setRol((Rol) cmbRol.getSelectedItem());

                if (usuarioDAO.actualizarUsuario(usuarioExistente)) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.");
                    llenarTablaUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este usuario?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminarUsuario(idUsuario)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.");
                llenarTablaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(0, 153, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }
}
