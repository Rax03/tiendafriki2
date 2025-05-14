package org.example.model.entidades;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
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

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @ColumnDefault("0")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "imagen")
    private String imagen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_categoria", nullable = false)
    private categorias idCategoria;

    @OneToMany(mappedBy = "idProducto")
    private Set<DetallesPedido> detallesPedidos = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "productos")
    private Set<Proveedore> proveedores = new LinkedHashSet<>();

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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
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

    public categorias getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(categorias idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Set<DetallesPedido> getDetallesPedidos() {
        return detallesPedidos;
    }

    public void setDetallesPedidos(Set<DetallesPedido> detallesPedidos) {
        this.detallesPedidos = detallesPedidos;
    }

    public Set<Proveedore> getProveedores() {
        return proveedores;
    }

    public void setProveedores(Set<Proveedore> proveedores) {
        this.proveedores = proveedores;
    }

}