package org.example.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.model.entity.Categoria;
import org.example.utils.HibernateUtil;

import java.util.List;

public class CategoriaDAO {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // Obtener todas las categorías
    public List<Categoria> obtenerTodasLasCategorias() {
        EntityManager em = emf.createEntityManager();
        List<Categoria> categorias = List.of(); // Devuelve lista vacía por defecto
        try {
            categorias = em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error al obtener las categorías: " + e.getMessage());
        } finally {
            em.close();
        }
        return categorias;
    }

    // Agregar una nueva categoría
    public boolean agregarCategoria(Categoria categoria) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(categoria);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al agregar la categoría: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Eliminar una categoría por ID
    public boolean eliminarCategoria(int idCategoria) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Categoria categoria = em.find(Categoria.class, idCategoria);
            if (categoria != null) {
                em.remove(categoria);
                transaction.commit();
                return true;
            } else {
                System.err.println("❌ Categoría no encontrada.");
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al eliminar la categoría: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Actualizar una categoría
    public boolean actualizarCategoria(Categoria categoria) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(categoria);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al actualizar la categoría: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Obtener categoría por ID
    public Categoria obtenerCategoriaPorId(int idCategoria) {
        EntityManager em = emf.createEntityManager();
        Categoria categoria = null;
        try {
            categoria = em.find(Categoria.class, idCategoria);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener la categoría por ID: " + e.getMessage());
        } finally {
            em.close();
        }
        return categoria;
    }

    // Obtener categorías junto con sus productos (carga LAZY forzada mediante LEFT JOIN FETCH)
    public List<Categoria> obtenerCategoriasConProductos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT c FROM Categoria c LEFT JOIN FETCH c.productos", Categoria.class)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error al obtener categorías con productos: " + e.getMessage());
            return List.of(); // Devuelve lista vacía en caso de error.
        } finally {
            em.close();
        }
    }
}
