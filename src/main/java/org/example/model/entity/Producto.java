package org.example.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "productos", schema = "tiendafriki")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Float precio;

    @ColumnDefault("0")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "imagen")
    private String imagen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria idCategoria;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @OneToMany(mappedBy = "idProducto")
    private Set<DetallesPedido> detallesPedidos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProducto")
    private Set<ProductoProveedor> productoProveedors = new LinkedHashSet<>();

    public Producto(Integer id, String nombre, String descripcion, Float precio, Integer stock, String imagen, Categoria idCategoria, Instant fecha, Set<DetallesPedido> detallesPedidos, Set<ProductoProveedor> productoProveedors) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
        this.idCategoria = idCategoria;
        this.fecha = fecha;
        this.detallesPedidos = detallesPedidos;
        this.productoProveedors = productoProveedors;
    }

    public Producto() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Set<DetallesPedido> getDetallesPedidos() {
        return detallesPedidos;
    }

    public void setDetallesPedidos(Set<DetallesPedido> detallesPedidos) {
        this.detallesPedidos = detallesPedidos;
    }

    public Set<ProductoProveedor> getProductoProveedors() {
        return productoProveedors;
    }

    public void setProductoProveedors(Set<ProductoProveedor> productoProveedors) {
        this.productoProveedors = productoProveedors;
    }

}