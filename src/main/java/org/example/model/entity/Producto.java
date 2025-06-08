package org.example.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos", schema = "tiendafriki")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "imagen")
    private String imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria idCategoria;

    @Column(name = "fecha", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)  // Hibernate reconoce que es generado en BD al insertar
    private LocalDateTime fecha;

    public Producto(String nombre, BigDecimal precio, int stock, String s1, Categoria categoriaPersistente) {

        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.imagen = s1;
        this.idCategoria = categoriaPersistente;
    }

    public Producto() {

    }

    // Getters y setters

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

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    // No setter para fecha si quieres que solo la base de datos la maneje
}
