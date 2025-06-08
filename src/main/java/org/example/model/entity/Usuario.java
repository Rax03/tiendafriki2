package org.example.model.entity;

import jakarta.persistence.*;
import org.example.model.entity.Enum.Rol;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios", schema = "tiendafriki")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "`contraseña_hash`", nullable = false)
    private String contraseñaHash;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @ColumnDefault("current_timestamp(6)")
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @OneToMany(mappedBy = "idUsuario")
    private Set<Pedido> pedidos = new LinkedHashSet<>();

    public Usuario(Integer id, String nombre, String email, String contraseñaHash, String salt, Rol rol, Instant fechaRegistro, Set<Pedido> pedidos) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contraseñaHash = contraseñaHash;
        this.salt = salt;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.pedidos = pedidos;
    }

    public Usuario() {
    }

    public Usuario(int i, String nombre, String email, String contraseña, Object o, Rol rol, LocalDate now) {
        this.id = i;
        this.nombre = nombre;
        this.email = email;
        this.contraseñaHash = contraseña;
        this.salt = (o != null) ? o.toString() : ""; // ✅ Se evita el NullPointerException
        this.rol = rol;
        this.fechaRegistro = now.atStartOfDay().toInstant(java.time.ZoneOffset.UTC); // ✅ Conversión correcta
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseñaHash() {
        return contraseñaHash;
    }

    public void setContraseñaHash(String contraseñaHash) {
        this.contraseñaHash = contraseñaHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", contraseñaHash='" + contraseñaHash + '\'' +
                ", salt='" + salt + '\'' +
                ", rol=" + rol +
                ", fechaRegistro=" + fechaRegistro +
                ", pedidos=" + pedidos +
                '}';
    }
}
