package org.example.view;

import org.example.model.dao.ProductoDAO;
import org.example.model.entity.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioVista extends JFrame {
    private JTextField campoBusqueda;
    private JButton botonBuscar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField campoCantidad;
    private JButton botonAgregarCarrito;
    private JButton botonEliminarCarrito;
    private JButton botonVerCarrito;
    private JButton botonFinalizarCompra;
    private JTextArea areaCarrito;

    private final ProductoDAO productoDAO = new ProductoDAO();

    public UsuarioVista() {
        setTitle("Tienda Friki - Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior: Búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        campoBusqueda = new JTextField();
        botonBuscar = new JButton("Buscar");
        panelBusqueda.add(new JLabel("Buscar producto: "), BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(botonBuscar, BorderLayout.EAST);
        add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de productos
        modeloTabla = new DefaultTableModel(new Object[]{"Imagen", "Nombre", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(80);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Productos disponibles"));
        add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior: Carrito y acciones
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));

        // Área carrito
        areaCarrito = new JTextArea();
        areaCarrito.setEditable(false);
        JScrollPane scrollCarrito = new JScrollPane(areaCarrito);
        scrollCarrito.setPreferredSize(new Dimension(300, 150));
        scrollCarrito.setBorder(BorderFactory.createTitledBorder("Carrito"));
        panelInferior.add(scrollCarrito, BorderLayout.EAST);

        // Panel de controles: cantidad y botones
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelControles.add(new JLabel("Cantidad:"));
        campoCantidad = new JTextField("1", 5);
        panelControles.add(campoCantidad);

        botonAgregarCarrito = new JButton("Agregar al carrito");
        botonEliminarCarrito = new JButton("Eliminar del carrito");
        botonVerCarrito = new JButton("Ver carrito");
        botonFinalizarCompra = new JButton("Finalizar compra");

        panelControles.add(botonAgregarCarrito);
        panelControles.add(botonEliminarCarrito);
        panelControles.add(botonVerCarrito);
        panelControles.add(botonFinalizarCompra);

        panelInferior.add(panelControles, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        cargarProductos();
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoDAO.obtenerProductosConCategoria();

        for (Producto p : productos) {
            System.out.println("Cargando producto: " + p.getNombre());

            // Cargar imagen correctamente
            ImageIcon icono = null;
            if (p.getImagen() != null && !p.getImagen().isEmpty()) {
                try {
                    Image img = new ImageIcon(p.getImagen()).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    icono = new ImageIcon(img);
                } catch (Exception ex) {
                    System.err.println("❌ Error al cargar imagen: " + ex.getMessage());
                }
            }

            modeloTabla.addRow(new Object[]{
                    icono,
                    p.getNombre(),
                    String.format("$%.2f", p.getPrecio()), // Usa directamente p.getPrecio()
                    p.getStock() > 0 ? p.getStock() + " unidades" : "Sin stock"
            });
        }

        // Aplicar CellRenderer correctamente después de llenar la tabla
        tablaProductos.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    JLabel label = new JLabel((ImageIcon) value);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }

    // Getters
    public JTextField getCampoBusqueda() { return campoBusqueda; }
    public JButton getBotonBuscar() { return botonBuscar; }
    public JTable getTablaProductos() { return tablaProductos; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JTextField getCampoCantidad() { return campoCantidad; }
    public JButton getBotonAgregarCarrito() { return botonAgregarCarrito; }
    public JButton getBotonEliminarCarrito() { return botonEliminarCarrito; }
    public JButton getBotonVerCarrito() { return botonVerCarrito; }
    public JButton getBotonFinalizarCompra() { return botonFinalizarCompra; }
    public JTextArea getAreaCarrito() { return areaCarrito; }
}
