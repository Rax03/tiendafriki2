package org.example.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.conection.ConexionBD;
import org.example.model.entity.Usuario;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

    public List<Usuario> obtenerTodosLosUsuarios() {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener todos los usuarios", e);
            return null;
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorEmail(String email) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
            query.setParameter("email", email);
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al buscar usuario por email", e);
            return null;
        } finally {
            em.close();
        }
    }

    public boolean registrarUsuario(Usuario usuario) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);  // persistir nuevo usuario
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            logger.log(Level.SEVERE, "Error al registrar usuario", e);
            return false;
        } finally {
            em.close();
        }
    }

    public boolean correoExiste(String email) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al verificar si el correo existe", e);
            return false;
        } finally {
            em.close();
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                em.remove(usuario);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            logger.log(Level.SEVERE, "Error al eliminar el usuario", e);
            return false;
        } finally {
            em.close();
        }
    }

    public boolean actualizarUsuario(Usuario usuario) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);  // merge para actualizar entidad existente
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            logger.log(Level.SEVERE, "Error al actualizar el usuario", e);
            return false;
        } finally {
            em.close();
        }
    }

    public Usuario obtenerDatosUsuario(int idUsuario) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            return em.find(Usuario.class, idUsuario);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener datos del usuario", e);
            return null;
        } finally {
            em.close();
        }
    }

    public String obtenerRolPorEmail(String email) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            TypedQuery<String> query = em.createQuery(
                    "SELECT u.rol FROM Usuario u WHERE u.email = :email", String.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener el rol del usuario por email", e);
            return null;
        } finally {
            em.close();
        }
    }

    public Usuario obtenerUsuarioPorId(int idUsuario) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            return em.find(Usuario.class, idUsuario);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener usuario por ID", e);
            return null;
        } finally {
            em.close();
        }
    }
}
