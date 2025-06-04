package org.example.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "pedidos", schema = "tiendafriki")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_cliente", nullable = false)
    private org.example.model.entity.Usuario idCliente;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Lob
    @Column(name = "direccion_envio", nullable = false)
    private String direccionEnvio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private org.example.model.entity.Usuario idUsuario;

    @OneToMany(mappedBy = "idPedido")
    private Set<DetallesPedido> detallesPedidos = new LinkedHashSet<>();

    public Pedido(Integer id, org.example.model.entity.Usuario idCliente, String estado, LocalDateTime fechaPedido, BigDecimal total, String direccionEnvio, org.example.model.entity.Usuario idUsuario, Set<DetallesPedido> detallesPedidos) {
        this.id = id;
        this.idCliente = idCliente;
        this.estado = estado;
        this.fechaPedido = fechaPedido;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        this.idUsuario = idUsuario;
        this.detallesPedidos = detallesPedidos;
    }

    public Pedido() {
    }

    public Pedido(int i, int i1, LocalDateTime fechaPedido, String string, float v) {
        this.id = i;
        this.idCliente = new org.example.model.entity.Usuario(
                i1,
                "Cliente" + i1,
                "cliente" + i1 + "@example.com",
                "hash",
                "salt",
                org.example.model.entity.Enum.Rol.CLIENTE,
                Instant.EPOCH, // Aquí se usa LocalDateTime en lugar de Instant
                null);
        this.fechaPedido = fechaPedido;
        this.estado = string;
        this.total = BigDecimal.valueOf(v);
        this.direccionEnvio = "Direccion de envio " + i;
        this.idUsuario = new org.example.model.entity.Usuario(
                i1,
                "Usuario" + i1,
                "usuario" + i1 + "@example.com",
                "hash",
                "salt",
                org.example.model.entity.Enum.Rol.ADMIN,
                Instant.now(), // Usando LocalDateTime aquí también
                null);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public org.example.model.entity.Usuario getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(org.example.model.entity.Usuario idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public org.example.model.entity.Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(org.example.model.entity.Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Set<DetallesPedido> getDetallesPedidos() {
        return detallesPedidos;
    }

    public void setDetallesPedidos(Set<DetallesPedido> detallesPedidos) {
        this.detallesPedidos = detallesPedidos;
    }
}
