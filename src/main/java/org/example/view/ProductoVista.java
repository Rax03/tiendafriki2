package org.example.view;

import org.example.model.dao.ProductosDAO;
import org.example.model.entity.Categoria;
import org.example.model.entity.Producto;
import org.example.model.entity.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoVista extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private ProductosDAO productosDAO;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    public ProductoVista() {
        setTitle("Gestión de Productos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        productosDAO = new ProductosDAO();

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Productos", JLabel.CENTER);
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        panelPrincipal.setBackground(new Color(34, 34, 34));

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Cantidad", "Categoría", "Proveedor", "Imagen"}, 0) {
            public Class<?> getColumnClass(int column) {
                return (column == 6) ? Icon.class : Object.class;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(60);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);

        // Agregar MouseListener para detectar clic en la tabla
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int filaSeleccionada = tablaProductos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                    Producto producto = productosDAO.obtenerProductoPorId(idProducto);
                    if (producto != null) {
                        mostrarDetallesProducto(producto);
                    }
                }
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnEditar.addActionListener(e -> mostrarFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarProducto());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        llenarTablaProductos();

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
        setVisible(true);
    }

    public void llenarTablaProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productosDAO.obtenerTodosLosProductos();

        for (Producto producto : productos) {
            ImageIcon icono = new ImageIcon(producto.getImagen());
            Image imagen = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

            // Asegurar la categoría no sea null
            String nombreCategoria = (producto.getId_categoria() != null) ? producto.getId_categoria().getNombre() : "Sin categoría";

            // Convertir lista de proveedores en texto legible
            String proveedores = "Sin proveedor";
            if (producto.getProveedores() != null && !producto.getProveedores().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Proveedor prov : producto.getProveedores()) {
                    sb.append(prov.getNombre()).append(", ");
                }
                proveedores = sb.substring(0, sb.length() - 2);
            }

            modeloTabla.addRow(new Object[]{
                    producto.getId_producto(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock(),
                    nombreCategoria,
                    proveedores,
                    new ImageIcon(imagen)
            });

            // Ocultar la columna ID
            tablaProductos.getColumnModel().getColumn(0).setMinWidth(0);
            tablaProductos.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaProductos.getColumnModel().getColumn(0).setWidth(0);
        }
    }


    public void mostrarFormularioAgregar() {
        // Campos de texto para los otros campos
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtCantidad = new JTextField();

        // JComboBox para seleccionar la categoría
        JComboBox<Categoria> comboCategoria = new JComboBox<>();
        // JComboBox para seleccionar el proveedor
        JComboBox<Proveedor> comboProveedor = new JComboBox<>();

        // Cargar las categorías y proveedores en los JComboBox
        cargarCategorias(comboCategoria);
        cargarProveedores(comboProveedor);

        // Etiqueta para la imagen
        JLabel lblImagen = new JLabel("Sin imagen");
        JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        final String[] rutaImagen = {null};

        btnSeleccionarImagen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                rutaImagen[0] = fileChooser.getSelectedFile().getAbsolutePath();
                lblImagen.setText(fileChooser.getSelectedFile().getName());
            }
        });

        // Panel del formulario con 6 filas y 2 columnas
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);
        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(comboCategoria);
        panelFormulario.add(new JLabel("Proveedor:"));
        panelFormulario.add(comboProveedor);
        panelFormulario.add(new JLabel("Imagen:"));
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.add(lblImagen, BorderLayout.CENTER);
        panelImagen.add(btnSeleccionarImagen, BorderLayout.EAST);
        panelFormulario.add(panelImagen);

        // Mostrar el cuadro de diálogo para agregar producto
        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                // Obtener la categoría y proveedor seleccionados
                Categoria categoria = (Categoria) comboCategoria.getSelectedItem();
                Proveedor proveedor = (Proveedor) comboProveedor.getSelectedItem();

                // Crear la lista de proveedores
                List<Proveedor> proveedores = new ArrayList<>();
                proveedores.add(proveedor);

                // Crear el producto
                Producto producto = new Producto(
                        0,
                        txtNombre.getText(),
                        "",
                        Float.parseFloat(txtPrecio.getText()),
                        Integer.parseInt(txtCantidad.getText()),
                        "",
                        categoria,
                        proveedores
                );

                producto.setImagen(rutaImagen[0]);



                // Agregar el producto a la base de datos
                boolean exito = productosDAO.agregarProducto(producto);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
                    llenarTablaProductos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarCategorias(JComboBox<Categoria> comboCategoria) {
        // Limpiar el ComboBox antes de llenarlo
        comboCategoria.removeAllItems();

        // Obtener todas las categorías desde el DAO
        List<Categoria> categorias = productosDAO.obtenerCategorias();

        if (categorias == null || categorias.isEmpty()) {
            System.err.println("⚠ No se encontraron categorías.");
            return; // Salir si no hay categorías disponibles
        }

        for (Categoria categoria : categorias) {
            comboCategoria.addItem(categoria); // Añadir cada categoría al combo
        }
    }


    private void cargarProveedores(JComboBox<Proveedor> comboProveedor) {
        // Obtener todos los proveedores desde el DAO
        List<Proveedor> proveedores = productosDAO.obtenerProveedores();
        for (Proveedor proveedor : proveedores) {
            comboProveedor.addItem(proveedor); // Añadir el proveedor al combo
        }
    }

    public void mostrarFormularioEditar() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        Producto producto = productosDAO.obtenerProductoPorId(idProducto);
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField txtNombre = new JTextField(producto.getNombre());
        JTextField txtPrecio = new JTextField(String.valueOf(producto.getPrecio()));
        JTextField txtCantidad = new JTextField(String.valueOf(producto.getStock()));
        JTextField txtCategoria = new JTextField(producto.getId_categoria().getNombre());
        JTextField txtProveedor = new JTextField(producto.getProveedores().get(0).getNombre());

        JLabel lblImagen = new JLabel(producto.getImagen() != null ? producto.getImagen() : "Sin imagen");
        JButton btnSeleccionarImagen = new JButton("Cambiar Imagen");
        final String[] rutaImagen = {producto.getImagen()};

        btnSeleccionarImagen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                rutaImagen[0] = fileChooser.getSelectedFile().getAbsolutePath();
                lblImagen.setText(fileChooser.getSelectedFile().getName());
            }
        });

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);
        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(txtCategoria);
        panelFormulario.add(new JLabel("Proveedor:"));
        panelFormulario.add(txtProveedor);
        panelFormulario.add(new JLabel("Imagen:"));
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.add(lblImagen, BorderLayout.CENTER);
        panelImagen.add(btnSeleccionarImagen, BorderLayout.EAST);
        panelFormulario.add(panelImagen);

        int opcion = JOptionPane.showConfirmDialog(this, panelFormulario, "Editar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                producto.setNombre(txtNombre.getText());
                producto.setPrecio(Float.parseFloat(txtPrecio.getText()));
                producto.setStock(Integer.parseInt(txtCantidad.getText()));
                producto.getId_categoria().setNombre(txtCategoria.getText());
                producto.getProveedores().get(0).setNombre(txtProveedor.getText());
                producto.setImagen(rutaImagen[0]);

                boolean exito = productosDAO.actualizarProducto(producto);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
                    llenarTablaProductos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void eliminarProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este producto?",
                "Eliminar Producto", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = productosDAO.eliminarProducto(idProducto);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                llenarTablaProductos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(0, 153, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void mostrarDetallesProducto(Producto producto) {
        // Crear un panel para mostrar los detalles
        JPanel panelDetalles = new JPanel(new GridLayout(6, 2, 10, 10));
        panelDetalles.add(new JLabel("ID Producto:"));
        panelDetalles.add(new JLabel(String.valueOf(producto.getId_producto())));
        panelDetalles.add(new JLabel("Nombre:"));
        panelDetalles.add(new JLabel(producto.getNombre()));
        panelDetalles.add(new JLabel("Precio:"));
        panelDetalles.add(new JLabel(String.valueOf(producto.getPrecio())));
        panelDetalles.add(new JLabel("Cantidad:"));
        panelDetalles.add(new JLabel(String.valueOf(producto.getStock())));
        panelDetalles.add(new JLabel("Categoría:"));
        panelDetalles.add(new JLabel(producto.getId_categoria().getNombre()));
        panelDetalles.add(new JLabel("Proveedor:"));
        panelDetalles.add(new JLabel(producto.getProveedores().get(0).getNombre()));

        // Si la imagen está disponible, agregarla
        if (producto.getImagen() != null) {
            ImageIcon icono = new ImageIcon(producto.getImagen());
            Image imagen = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel lblImagen = new JLabel(new ImageIcon(imagen));
            panelDetalles.add(new JLabel("Imagen:"));
            panelDetalles.add(lblImagen);
        } else {
            panelDetalles.add(new JLabel("Imagen:"));
            panelDetalles.add(new JLabel("No disponible"));
        }

        // Mostrar un cuadro de diálogo con los detalles del producto
        JOptionPane.showMessageDialog(this, panelDetalles, "Detalles del Producto", JOptionPane.INFORMATION_MESSAGE);
    }

    public Producto getProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            return null; // No hay fila seleccionada
        }

        int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0); // Supone que la primera columna contiene el ID
        return productosDAO.obtenerProductoPorId(idProducto);
    }

}
