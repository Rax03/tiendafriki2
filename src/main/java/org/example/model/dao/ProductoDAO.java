package org.example.model.dao;

import jakarta.persistence.*;
import org.example.model.entity.Categoria;
import org.example.model.entity.Producto;

import java.util.List;

public class ProductoDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("tiendafriki");

    public boolean agregarProducto(Producto producto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Obtener la categoria managed por el EntityManager
            Categoria categoriaManaged = em.find(Categoria.class, producto.getIdCategoria().getId());
            producto.setIdCategoria(categoriaManaged);

            em.merge(producto);

            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al agregar producto: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }


    public void actualizarProducto(Producto producto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Obtener la categoría gestionada
            Categoria categoria = em.find(Categoria.class, producto.getIdCategoria().getId());
            producto.setIdCategoria(categoria);

            em.merge(producto);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void eliminarProducto(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Producto producto = em.find(Producto.class, id);
            if (producto != null) {
                em.remove(producto);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Producto obtenerProductoPorId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public List<Producto> obtenerProductosConCategoria() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p JOIN FETCH p.idCategoria", Producto.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Categoria> obtenerCategorias() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Categoria obtenerCategoriaPorId(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public List<Producto> obtenerTodosLosProductos() {
        EntityManager em = emf.createEntityManager();
        try {
            // Para traer productos con categorías, usar JOIN FETCH
            return em.createQuery("SELECT p FROM Producto p JOIN FETCH p.idCategoria", Producto.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Producto obtenerProductoConCategoriaPorId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Producto p JOIN FETCH p.idCategoria WHERE p.id = :id", Producto.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }


}
