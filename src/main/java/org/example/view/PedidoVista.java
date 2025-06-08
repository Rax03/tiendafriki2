package org.example.view;

import org.example.model.dao.PedidoDAO;
import org.example.model.dao.UsuarioDAO;
import org.example.model.entity.Pedido;
import org.example.model.entity.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidoVista extends JFrame {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private PedidoDAO pedidoDAO;
    private UsuarioDAO usuarioDAO;

    public PedidoVista() {
        setTitle("Gestión de Pedidos - Tienda Friki");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        pedidoDAO = new PedidoDAO();
        usuarioDAO = new UsuarioDAO(); // Inicialización del DAO de Usuario

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Pedidos", JLabel.CENTER);
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        panelPrincipal.setBackground(new Color(34, 34, 34));

        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{
                "ID Pedido", "Cliente", "Fecha Pedido", "Estado", "Total", "Productos"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaPedidos);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarPedido());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Agregar componentes al panel principal
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        llenarTablaPedidos(); // Llenar datos al iniciar
    }

    private void llenarTablaPedidos() {
        modeloTabla.setRowCount(0);
        List<Pedido> pedidos = pedidoDAO.obtenerTodosLosPedidos();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Pedido pedido : pedidos) {
            String nombreCliente = pedidoDAO.obtenerNombreClientePorId(pedido.getId());
            String productos = pedidoDAO.obtenerProductosPorPedido(pedido.getId());
            modeloTabla.addRow(new Object[]{
                    pedido.getId(),
                    (nombreCliente != null) ? nombreCliente : "Cliente desconocido",
                    pedido.getFechaPedido().format(formatter),
                    pedido.getEstado(),
                    pedido.getTotal(),
                    (productos != null) ? productos : "Sin productos"
            });
        }
    }

    private void mostrarFormularioAgregar() {
        // Solicitamos el ID del usuario, el total y seleccionamos el estado
        JTextField txtIdUsuario = new JTextField();
        JTextField txtTotal = new JTextField();
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Pendiente", "Enviado", "Entregado", "Cancelado"});
        LocalDateTime fechaPedido = LocalDateTime.now();

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.add(new JLabel("ID Usuario:"));
        panelFormulario.add(txtIdUsuario);
        panelFormulario.add(new JLabel("Total:"));
        panelFormulario.add(txtTotal);
        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(cmbEstado);
        panelFormulario.add(new JLabel("Fecha (automática):"));
        panelFormulario.add(new JLabel(fechaPedido.toString()));

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Agregar Pedido", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                // Pedir dirección de envío
                String direccionEnvio = JOptionPane.showInputDialog("Ingrese su dirección de envío:");
                if (direccionEnvio == null || direccionEnvio.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar una dirección válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Obtener el usuario desde la base de datos
                int idUsuario = Integer.parseInt(txtIdUsuario.getText().trim());
                Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
                if (usuario == null) {
                    JOptionPane.showMessageDialog(this, "❌ Error: Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear el pedido con la dirección y usuario asignados
                Pedido pedido = new Pedido(
                        cmbEstado.getSelectedItem().toString(),
                        fechaPedido,
                        new BigDecimal(txtTotal.getText().trim()),
                        direccionEnvio,
                        usuario
                );

                if (pedidoDAO.insertarPedido(pedido)) {
                    JOptionPane.showMessageDialog(this, "✅ Pedido registrado exitosamente.");
                    llenarTablaPedidos();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al registrar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "❌ Error en los datos ingresados. Verifica los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarFormularioEditar() {
        int filaSeleccionada = tablaPedidos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido para editar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPedido = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        Pedido pedidoExistente = pedidoDAO.obtenerPedidoPorId(idPedido);
        if (pedidoExistente == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos del pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // En la edición, solo permitimos cambiar el total y el estado.
        JTextField txtTotal = new JTextField(pedidoExistente.getTotal().toString());
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Pendiente", "Enviado", "Entregado", "Cancelado"});
        cmbEstado.setSelectedItem(pedidoExistente.getEstado());

        // Mostramos información no editable: ID Pedido, Cliente y Fecha Pedido.
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.add(new JLabel("ID Pedido:"));
        panelFormulario.add(new JLabel(String.valueOf(pedidoExistente.getId())));
        panelFormulario.add(new JLabel("Cliente:"));
        panelFormulario.add(new JLabel(pedidoDAO.obtenerNombreClientePorId(pedidoExistente.getId())));
        panelFormulario.add(new JLabel("Total:"));
        panelFormulario.add(txtTotal);
        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(cmbEstado);
        panelFormulario.add(new JLabel("Fecha Pedido:"));
        panelFormulario.add(new JLabel(pedidoExistente.getFechaPedido().toString()));

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Editar Pedido", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                // Actualizar solo los campos editables: Total y Estado
                pedidoExistente.setTotal(new BigDecimal(txtTotal.getText().trim()));
                pedidoExistente.setEstado(cmbEstado.getSelectedItem().toString());
                if (pedidoDAO.actualizarPedido(pedidoExistente)) {
                    JOptionPane.showMessageDialog(this, "Pedido actualizado exitosamente.");
                    llenarTablaPedidos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados. Verifica los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarPedido() {
        int filaSeleccionada = tablaPedidos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPedido = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este pedido?", "Eliminar Pedido", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (pedidoDAO.eliminarPedido(idPedido)) {
                JOptionPane.showMessageDialog(this, "Pedido eliminado exitosamente.");
                llenarTablaPedidos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
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
