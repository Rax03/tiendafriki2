package org.example.view;

import org.example.model.conection.ConexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioVista extends JFrame {
    private JTextField campoBusqueda;
    private JButton botonBuscar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton botonAgregarCarrito;
    private JButton botonEliminarCarrito;
    private JButton botonVerCarrito;
    private JButton botonFinalizarCompra;
    private JTextArea areaCarrito;
    private JTextField campoCantidad;

    public UsuarioVista() {
        setTitle("Tienda Friki - Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de búsqueda
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.LINE_AXIS));
        campoBusqueda = new JTextField();
        botonBuscar = new JButton("🔍 Buscar");

        panelSuperior.add(campoBusqueda);
        panelSuperior.add(botonBuscar);
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Buscar Productos"));
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla de productos
        modeloTabla = new DefaultTableModel(new Object[]{"Imagen", "Nombre", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? ImageIcon.class : Object.class;
            }
        };

        tablaProductos.setRowHeight(100);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de Productos"));
        add(scrollTabla, BorderLayout.CENTER);

        // Panel de carrito y cantidad
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        JPanel panelCarrito = new JPanel(new GridLayout(2, 1));
        areaCarrito = new JTextArea(5, 40);
        areaCarrito.setEditable(false);
        panelCarrito.add(new JScrollPane(areaCarrito));

        JPanel panelCantidad = new JPanel(new FlowLayout());
        panelCantidad.add(new JLabel("Cantidad:"));
        campoCantidad = new JTextField("1", 5);
        panelCantidad.add(campoCantidad);

        botonAgregarCarrito = new JButton("🛒 Agregar al carrito");
        botonEliminarCarrito = new JButton("❌ Eliminar del carrito");
        botonVerCarrito = new JButton("📦 Ver carrito");
        botonFinalizarCompra = new JButton("✅ Finalizar compra");

        panelCantidad.add(botonAgregarCarrito);
        panelCantidad.add(botonEliminarCarrito);
        panelCantidad.add(botonVerCarrito);
        panelCantidad.add(botonFinalizarCompra);

        panelCarrito.add(panelCantidad);
        panelInferior.add(panelCarrito, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Evento para finalizar compra y reducir stock
        botonFinalizarCompra.addActionListener(e -> {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada != -1) {
                int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 1); // Suponiendo que el ID está en la columna 1

                try (Connection conexion = ConexionBD.conectar();
                     PreparedStatement stmt = conexion.prepareStatement("UPDATE productos SET stock = stock - ? WHERE id_producto = ?")) {

                    // ✅ Convertir el contenido del JTextField a un número
                    int cantidadSeleccionada;
                    try {
                        cantidadSeleccionada = Integer.parseInt(campoCantidad.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "❌ Ingresa un número válido en la cantidad.");
                        return;
                    }

                    stmt.setInt(1, cantidadSeleccionada);
                    stmt.setInt(2, idProducto);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "✅ Compra realizada. Stock actualizado.");
                } catch (SQLException ex) {
                    System.err.println("❌ Error al actualizar stock: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Error al procesar la compra.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "❌ Ingresa un número válido en el campo de cantidad.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto antes de finalizar la compra.");
            }
        });




    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JTextField getCampoBusqueda() { return campoBusqueda; }
    public JButton getBotonBuscar() { return botonBuscar; }
    public JTable getTablaProductos() { return tablaProductos; }
    public JTextField getCampoCantidad() { return campoCantidad; }
    public JTextArea getAreaCarrito() { return areaCarrito; }
    public JButton getBotonAgregarCarrito() { return botonAgregarCarrito; }
    public JButton getBotonEliminarCarrito() { return botonEliminarCarrito; }
    public JButton getBotonVerCarrito() { return botonVerCarrito; }
    public JButton getBotonFinalizarCompra() { return botonFinalizarCompra; }
}
