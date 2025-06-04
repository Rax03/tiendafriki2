package org.example.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.entity.Usuario;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("tiendafriki");

    public List<Usuario> obtenerTodosLosUsuarios() {
        EntityManager em = emf.createEntityManager();
        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        em.close();
        return usuarios;
    }

    public Usuario buscarPorEmail(String email) {
        EntityManager em = emf.createEntityManager();
        Usuario usuario = null;
        try {
            usuario = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Usuario no encontrado: " + email);
        } finally {
            em.close();
        }
        return usuario;
    }

    public boolean registrarUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al registrar usuario", e);
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean correoExiste(String email) {
        return buscarPorEmail(email) != null;
    }

    public boolean eliminarUsuario(int idUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                em.getTransaction().begin();
                em.remove(usuario);
                em.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al eliminar usuario", e);
        } finally {
            em.close();
        }
        return false;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al actualizar usuario", e);
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public String obtenerRolPorEmail(String email) {
        Usuario usuario = buscarPorEmail(email);
        return (usuario != null) ? usuario.getRol().name() : null;
    }


    public Usuario obtenerDatosUsuario(int idUsuario) {

        EntityManager em = emf.createEntityManager();
        Usuario usuario = null;
        try {
            usuario = em.find(Usuario.class, idUsuario);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener datos del usuario", e);
        } finally {
            em.close();
        }
        return usuario;
    }
}
