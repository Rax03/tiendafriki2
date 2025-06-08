package org.example.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.model.entity.Proveedor;
import org.example.utils.HibernateUtil;

import java.util.List;

public class ProveedorDAO {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // Obtener todos los proveedores
    public List<Proveedor> obtenerTodosLosProveedores() {
        EntityManager em = emf.createEntityManager();
        List<Proveedor> proveedores = List.of(); // Devuelve lista vacía por defecto
        try {
            proveedores = em.createQuery("SELECT p FROM Proveedor p", Proveedor.class)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error al obtener proveedores: " + e.getMessage());
        } finally {
            em.close();
        }
        return proveedores;
    }

    // Agregar nuevo proveedor
    public boolean agregarProveedor(Proveedor proveedor) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(proveedor);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al agregar proveedor: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Eliminar proveedor por ID
    public boolean eliminarProveedor(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Proveedor proveedor = em.find(Proveedor.class, id);
            if (proveedor != null) {
                em.remove(proveedor);
                transaction.commit();
                return true;
            } else {
                System.err.println("❌ Proveedor no encontrado.");
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al eliminar proveedor: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Actualizar proveedor existente
    public boolean actualizarProveedor(Proveedor proveedor) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(proveedor);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al actualizar proveedor: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Obtener proveedor por ID
    public Proveedor obtenerProveedorPorId(int id) {
        EntityManager em = emf.createEntityManager();
        Proveedor proveedor = null;
        try {
            proveedor = em.find(Proveedor.class, id);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener proveedor por ID: " + e.getMessage());
        } finally {
            em.close();
        }
        return proveedor;
    }
}
