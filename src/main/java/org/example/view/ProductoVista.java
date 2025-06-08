package org.example.view;

import org.example.model.dao.ProductoDAO;
import org.example.model.entity.Categoria;
import org.example.model.entity.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ProductoVista extends JFrame {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private JTable tabla;
    private DefaultTableModel modelo;

    public ProductoVista() {
        setTitle("Gestión de Productos");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);

        actualizarTabla();
        setVisible(true);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel();
        JLabel titulo = new JLabel("Gestión de Productos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo);
        return panel;
    }

    private JScrollPane crearTabla() {
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Categoría", "Imagen"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Icon.class : Object.class;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(60);
        return new JScrollPane(tabla);
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel();

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        btnAgregar.addActionListener(e -> mostrarFormulario(null));
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        panel.add(btnAgregar);
        panel.add(btnEditar);
        panel.add(btnEliminar);

        return panel;
    }

    private void mostrarFormulario(Producto producto) {
        JTextField txtNombre = new JTextField(producto != null ? producto.getNombre() : "");
        JTextField txtPrecio = new JTextField(producto != null ? String.valueOf(producto.getPrecio()) : "");
        JTextField txtStock = new JTextField(producto != null ? String.valueOf(producto.getStock()) : "");

        // Campo fecha creación editable (formato ISO_LOCAL_DATE_TIME)
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String fechaDefault = producto != null && producto.getFecha() != null
                ? producto.getFecha().format(formatter)
                : LocalDateTime.now().format(formatter);
        JTextField txtFechaCreacion = new JTextField(fechaDefault);

        JComboBox<Categoria> comboCategoria = new JComboBox<>();
        cargarCategorias(comboCategoria);
        if (producto != null && producto.getIdCategoria() != null) {
            comboCategoria.setSelectedItem(producto.getIdCategoria());
        }

        JLabel lblImagen = new JLabel(producto != null && producto.getImagen() != null ? producto.getImagen() : "Sin imagen");
        JButton btnImagen = new JButton("Seleccionar Imagen");
        final String[] rutaImagen = {producto != null ? producto.getImagen() : null};

        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaImagen[0] = fc.getSelectedFile().getAbsolutePath();
                lblImagen.setText(fc.getSelectedFile().getName());
            }
        });

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Precio:"));
        form.add(txtPrecio);
        form.add(new JLabel("Stock:"));
        form.add(txtStock);
        form.add(new JLabel("Categoría:"));
        form.add(comboCategoria);
        form.add(new JLabel("Fecha de creación (yyyy-MM-ddTHH:mm:ss):"));
        form.add(txtFechaCreacion);

        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.add(lblImagen, BorderLayout.CENTER);
        panelImagen.add(btnImagen, BorderLayout.EAST);
        form.add(new JLabel("Imagen:"));
        form.add(panelImagen);

        int opcion = JOptionPane.showConfirmDialog(this, form,
                producto == null ? "Nuevo Producto" : "Editar Producto", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText().trim();
                BigDecimal precio = new BigDecimal(txtPrecio.getText().trim()); // Usa BigDecimal en lugar de float
                int stock = Integer.parseInt(txtStock.getText().trim());
                Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

                if (categoria == null || categoria.getId() == null) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDateTime fechaCreacion;
                try {
                    fechaCreacion = LocalDateTime.parse(txtFechaCreacion.getText().trim(), formatter);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Fecha inválida. Usa el formato yyyy-MM-ddTHH:mm:ss", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Categoria categoriaPersistente = productoDAO.obtenerCategoriaPorId(categoria.getId());

                if (producto == null) {
                    Producto nuevo = new Producto(nombre, precio, stock, rutaImagen[0], categoriaPersistente);
                    productoDAO.agregarProducto(nuevo);
                    JOptionPane.showMessageDialog(this, "Producto agregado.");
                } else {
                    producto.setNombre(nombre);
                    producto.setPrecio(precio); // Usa BigDecimal en lugar de float
                    producto.setStock(stock);
                    producto.setImagen(rutaImagen[0]);
                    producto.setIdCategoria(categoriaPersistente);
                    productoDAO.actualizarProducto(producto);
                    JOptionPane.showMessageDialog(this, "Producto actualizado.");
                }

                actualizarTabla();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Formato numérico inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


            private void editarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.");
            return;
        }
        Integer id = (Integer) modelo.getValueAt(fila, 0);
        Producto producto = productoDAO.obtenerProductoConCategoriaPorId(id);
        if (producto != null) {
            mostrarFormulario(producto);
        }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            return;
        }
        Integer id = (Integer) modelo.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productoDAO.eliminarProducto(id);
            actualizarTabla();
        }
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        List<Producto> productos = productoDAO.obtenerProductosConCategoria();
        for (Producto p : productos) {
            ImageIcon icono = null;
            if (p.getImagen() != null) {
                Image img = new ImageIcon(p.getImagen()).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                icono = new ImageIcon(img);
            }
            modelo.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock(),
                    (p.getIdCategoria() != null ? p.getIdCategoria().getNombre() : "Sin categoría"),
                    icono
            });
        }
    }

    private void cargarCategorias(JComboBox<Categoria> combo) {
        combo.removeAllItems();
        List<Categoria> categorias = productoDAO.obtenerCategorias();
        for (Categoria c : categorias) {
            combo.addItem(c);
        }
    }

}
