package entidades;

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
    private entidades.Producto idProducto;

    @MapsId("idProveedor")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private entidades.Proveedore idProveedor;

    public ProductoProveedorId getId() {
        return id;
    }

    public void setId(ProductoProveedorId id) {
        this.id = id;
    }

    public entidades.Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(entidades.Producto idProducto) {
        this.idProducto = idProducto;
    }

    public entidades.Proveedore getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(entidades.Proveedore idProveedor) {
        this.idProveedor = idProveedor;
    }

}