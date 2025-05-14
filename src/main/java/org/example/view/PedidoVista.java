package org.example.view;

import org.example.model.dao.PedidoDAO;
import org.example.model.entity.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidoVista extends JFrame {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private PedidoDAO pedidoDAO;

    public PedidoVista() {
        setTitle("Gestión de Pedidos - Tienda Friki");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        pedidoDAO = new PedidoDAO();

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

        for (Pedido pedido : pedidos) {
            String nombreCliente = pedidoDAO.obtenerNombreClientePorId(pedido.getIdCliente());
            String productos = pedidoDAO.obtenerProductosPorPedido(pedido.getIdPedido());

            modeloTabla.addRow(new Object[]{
                    pedido.getIdPedido(),
                    (nombreCliente != null) ? nombreCliente : "Cliente desconocido",
                    pedido.getFechaPedido().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    pedido.getEstado(),
                    pedido.getTotal(),
                    (productos != null) ? productos : "Sin productos",
            });
        }
    }

    private void mostrarFormularioAgregar() {
        JTextField txtIdCliente = new JTextField();
        JTextField txtTotal = new JTextField();
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Pendiente", "Enviado", "Entregado", "Cancelado"});
        LocalDateTime fechaPedido = LocalDateTime.now();

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.add(new JLabel("ID Cliente:"));
        panelFormulario.add(txtIdCliente);
        panelFormulario.add(new JLabel("Total:"));
        panelFormulario.add(txtTotal);
        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(cmbEstado);
        panelFormulario.add(new JLabel("Fecha (automática):"));
        panelFormulario.add(new JLabel(fechaPedido.toString()));

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Agregar Pedido", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                Pedido pedido = new Pedido(
                        0,
                        Integer.parseInt(txtIdCliente.getText()),
                        fechaPedido,
                        cmbEstado.getSelectedItem().toString(),
                        Float.parseFloat(txtTotal.getText())
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

        JTextField txtIdCliente = new JTextField(String.valueOf(pedidoExistente.getIdCliente()));
        JTextField txtTotal = new JTextField(String.valueOf(pedidoExistente.getTotal()));
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Pendiente", "Enviado", "Entregado", "Cancelado"});
        cmbEstado.setSelectedItem(pedidoExistente.getEstado());

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.add(new JLabel("ID Cliente:"));
        panelFormulario.add(txtIdCliente);
        panelFormulario.add(new JLabel("Total:"));
        panelFormulario.add(txtTotal);
        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(cmbEstado);
        panelFormulario.add(new JLabel("Fecha Pedido (no editable):"));
        panelFormulario.add(new JLabel(pedidoExistente.getFechaPedido().toString()));

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Editar Pedido", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                pedidoExistente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));
                pedidoExistente.setTotal(Float.parseFloat(txtTotal.getText()));
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
