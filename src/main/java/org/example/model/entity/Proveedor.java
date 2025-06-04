package org.example.model.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "proveedores", schema = "tiendafriki")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "idProveedor")
    private Set<ProductoProveedor> productoProveedors = new LinkedHashSet<>();

    public Proveedor(int i, String text, String text1, String text2, String text3) {

        this.id = i;
        this.nombre = text;
        this.direccion = text1;
        this.telefono = text2;
        this.email = text3;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public Proveedor(Integer id, String nombre, String direccion, String telefono, String email, Set<ProductoProveedor> productoProveedors) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.productoProveedors = productoProveedors;
    }

    public Proveedor() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<ProductoProveedor> getProductoProveedors() {
        return productoProveedors;
    }

    public void setProductoProveedors(Set<ProductoProveedor> productoProveedors) {
        this.productoProveedors = productoProveedors;
    }
}