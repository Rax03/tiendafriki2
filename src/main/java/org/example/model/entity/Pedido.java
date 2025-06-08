package org.example.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
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
    private Usuario idUsuario;

    @OneToMany(mappedBy = "idPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetallesPedido> detallesPedidos = new LinkedHashSet<>();

    public Pedido(String estado, LocalDateTime fechaPedido, BigDecimal total, String direccionEnvio, Usuario idUsuario) {
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        this.idUsuario = idUsuario;
        this.detallesPedidos = new LinkedHashSet<>(); // Evita problemas de null
    }

    public Pedido() {}

    public Integer getId() {
        return id;
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

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Set<DetallesPedido> getDetallesPedidos() {
        return detallesPedidos;
    }

    public void setDetallesPedidos(Set<DetallesPedido> detallesPedidos) {
        this.detallesPedidos = detallesPedidos;
    }
}
