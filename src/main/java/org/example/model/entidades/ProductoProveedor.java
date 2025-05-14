package org.example.model.entidades;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "producto_proveedor", schema = "tiendafriki")
public class ProductoProveedor {
    @EmbeddedId
    private ProductoProveedorId id;

    @MapsId("idProducto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto idProducto;

    @MapsId("idProveedor")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedore idProveedor;

    public ProductoProveedorId getId() {
        return id;
    }

    public void setId(ProductoProveedorId id) {
        this.id = id;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Proveedore getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedore idProveedor) {
        this.idProveedor = idProveedor;
    }

}