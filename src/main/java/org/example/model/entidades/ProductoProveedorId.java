package org.example.model.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class ProductoProveedorId implements java.io.Serializable {
    private static final long serialVersionUID = -5393031152086949669L;
    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "id_proveedor", nullable = false)
    private Integer idProveedor;

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductoProveedorId entity = (ProductoProveedorId) o;
        return Objects.equals(this.idProveedor, entity.idProveedor) &&
                Objects.equals(this.idProducto, entity.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProveedor, idProducto);
    }

}