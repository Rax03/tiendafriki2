package org.example.controller;

import org.example.model.dao.ProductoDAO;
import org.example.view.ProductoVista;

import javax.swing.*;

public class ProductoControlador {

    private ProductoVista vista;
    private ProductoDAO productoDAO;

    public ProductoControlador() {
        this.productoDAO = new ProductoDAO();
        this.vista = new ProductoVista();
    }

    public void iniciar() {
        SwingUtilities.invokeLater(() -> vista.setVisible(true));
    }


}
