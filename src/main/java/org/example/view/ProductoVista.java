package org.example.view;

import org.example.model.entity.Producto;
import org.example.model.entity.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoVista extends JFrame {

    private JTable tablaProductos;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    public ProductoVista() {
        setTitle("Tiendafriki - Gestión de Productos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Crear la tabla y colocarla en un JScrollPane.
        tablaProductos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // Panel con los botones.
        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Layout del frame.
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Método para actualizar la tabla de productos.
    public void llenarTablaProductos(List<Producto> productos) {
        String[] columnNames = {"ID", "Nombre", "Descripción", "Precio", "Stock", "Imagen", "Fecha", "Categoría"};
        DefaultTableModel modelo = new DefaultTableModel(columnNames, 0);
        for (Producto p : productos) {
            // Se asume que la categoría tiene un getter 'getNombre()'.
            Categoria cat = p.getIdCategoria();
            Object[] fila = {
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getPrecio(),
                    p.getStock(),
                    p.getImagen(),
                    p.getFecha(),
                    (cat != null ? cat.getNombre() : "Sin categoría")
            };
            modelo.addRow(fila);
        }
        tablaProductos.setModel(modelo);
    }

    // Getters para los botones.
    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    // Retorna el producto seleccionado en la tabla.
    // Para simplificar, se asume que el ID está en la primera columna.
    public Producto getProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada != -1) {
            try {
                int id = (int) tablaProductos.getValueAt(filaSeleccionada, 0);
                Producto prod = new Producto();
                prod.setId(id);
                return prod;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al obtener el producto seleccionado: " + e.getMessage());
            }
        }
        return null;
    }

    // Muestra un formulario para agregar un producto.
    public void mostrarFormularioAgregar() {
        ProductoForm form = new ProductoForm(this, "Agregar Producto");
        form.setVisible(true);
    }

    // Muestra un formulario para editar el producto seleccionado.
    public void mostrarFormularioEditar() {
        Producto producto = getProductoSeleccionado();
        if (producto != null) {
            ProductoForm form = new ProductoForm(this, "Editar Producto", producto);
            form.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.");
        }
    }

    // Diálogo interno para agregar o editar un producto.
    public static class ProductoForm extends JDialog {
        private JTextField txtNombre;
        private JTextArea txtDescripcion;
        private JTextField txtPrecio;
        private JTextField txtStock;
        private JTextField txtImagen;
        private JButton btnGuardar;
        private JButton btnCancelar;

        // Constructor para agregar.
        public ProductoForm(Frame owner, String title) {
            super(owner, title, true);
            initComponents();
        }

        // Constructor para editar.
        public ProductoForm(Frame owner, String title, Producto producto) {
            this(owner, title);
            txtNombre.setText(producto.getNombre());
            txtDescripcion.setText(producto.getDescripcion());
            txtPrecio.setText(producto.getPrecio() != null ? producto.getPrecio().toString() : "");
            txtStock.setText(producto.getStock() != null ? producto.getStock().toString() : "");
            txtImagen.setText(producto.getImagen());
            // Los demás datos (como categoría y fecha) se podrían gestionar con controles adicionales.
        }

        private void initComponents() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campo Nombre.
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(new JLabel("Nombre:"), gbc);
            txtNombre = new JTextField(20);
            gbc.gridx = 1;
            add(txtNombre, gbc);

            // Campo Descripción.
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(new JLabel("Descripción:"), gbc);
            txtDescripcion = new JTextArea(4, 20);
            JScrollPane spDescripcion = new JScrollPane(txtDescripcion);
            gbc.gridx = 1;
            add(spDescripcion, gbc);

            // Campo Precio.
            gbc.gridx = 0;
            gbc.gridy = 2;
            add(new JLabel("Precio:"), gbc);
            txtPrecio = new JTextField(10);
            gbc.gridx = 1;
            add(txtPrecio, gbc);

            // Campo Stock.
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(new JLabel("Stock:"), gbc);
            txtStock = new JTextField(10);
            gbc.gridx = 1;
            add(txtStock, gbc);

            // Campo Imagen.
            gbc.gridx = 0;
            gbc.gridy = 4;
            add(new JLabel("Imagen:"), gbc);
            txtImagen = new JTextField(20);
            gbc.gridx = 1;
            add(txtImagen, gbc);

            // Panel de botones.
            btnGuardar = new JButton("Guardar");
            btnCancelar = new JButton("Cancelar");
            JPanel panelBotones = new JPanel();
            panelBotones.add(btnGuardar);
            panelBotones.add(btnCancelar);
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            add(panelBotones, gbc);

            pack();
            setLocationRelativeTo(getOwner());

            btnCancelar.addActionListener(e -> dispose());
            // El botón guardar debería obtener los datos ingresados y pasarlos al controlador (por ejemplo, con un callback o actualizando la base de datos a través del DAO).
        }
    }
}
