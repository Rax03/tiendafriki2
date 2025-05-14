package org.example.view;

import org.example.model.dao.ProveedorDAO;
import org.example.model.entity.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProveedorVista extends JFrame {

    private JTable tablaProveedores;
    private DefaultTableModel modeloTabla;
    private ProveedorDAO proveedorDAO;

    public ProveedorVista() {
        setTitle("Gestión de Proveedores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        proveedorDAO = new ProveedorDAO();

        // Configurar panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Proveedores", JLabel.CENTER);
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        panelPrincipal.setBackground(new Color(34, 34, 34));

        // Configurar tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Teléfono", "Dirección", "Email"}, 0);
        tablaProveedores = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProveedores);

        // Configurar botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarProveedor());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Llenar la tabla con los datos de los proveedores
        llenarTablaProveedores();

        // Agregar componentes al panel principal
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Agregar panel principal al JFrame
        add(panelPrincipal);
        setVisible(true);
    }

    private void llenarTablaProveedores() {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de llenarla
        List<Proveedor> proveedores = proveedorDAO.obtenerTodosLosProveedores();
        for (Proveedor proveedor : proveedores) {
            modeloTabla.addRow(new Object[]{
                    proveedor.getId(),
                    proveedor.getNombre(),
                    proveedor.getTelefono(),
                    proveedor.getDireccion(),
                    proveedor.getEmail()
            });
        }
    }

    private void mostrarFormularioAgregar() {
        // Crear el formulario para agregar un proveedor
        JTextField txtNombre = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtEmail = new JTextField();

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);
        panelFormulario.add(new JLabel("Email:"));
        panelFormulario.add(txtEmail);

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Agregar Proveedor", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                Proveedor proveedor = new Proveedor(
                        0,
                        txtNombre.getText(),
                        txtDireccion.getText(),
                        txtTelefono.getText(),
                        txtEmail.getText()
                );
                boolean exito = proveedorDAO.agregarProveedor(proveedor);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Proveedor agregado correctamente.");
                    llenarTablaProveedores();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarFormularioEditar() {
        int filaSeleccionada = tablaProveedores.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor para editar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProveedor = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        Proveedor proveedor = proveedorDAO.obtenerProveedorPorId(idProveedor);
        if (proveedor == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener el proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField txtNombre = new JTextField(proveedor.getNombre());
        JTextField txtTelefono = new JTextField(proveedor.getTelefono());
        JTextField txtDireccion = new JTextField(proveedor.getDireccion());
        JTextField txtEmail = new JTextField(proveedor.getEmail());

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);
        panelFormulario.add(new JLabel("Email:"));
        panelFormulario.add(txtEmail);

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Editar Proveedor", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                proveedor.setNombre(txtNombre.getText());
                proveedor.setTelefono(txtTelefono.getText());
                proveedor.setDireccion(txtDireccion.getText());
                proveedor.setEmail(txtEmail.getText());

                boolean exito = proveedorDAO.actualizarProveedor(proveedor);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Proveedor actualizado correctamente.");
                    llenarTablaProveedores();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProveedor() {
        int filaSeleccionada = tablaProveedores.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProveedor = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este proveedor?",
                "Eliminar Proveedor", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = proveedorDAO.eliminarProveedor(idProveedor);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente.");
                llenarTablaProveedores();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(0, 153, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
    }
}
