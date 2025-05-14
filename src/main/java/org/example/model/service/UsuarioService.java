package org.example.model.service;

import org.example.model.dao.UsuarioDAO;
import org.example.model.entity.Usuario;
import org.example.utils.HashUtil;

import java.util.logging.Logger;

public class UsuarioService {

    private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());
    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticarUsuario(String email, String password) {
        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            if (usuario == null) {
                logger.warning("❌ Usuario no encontrado: " + email);
                return null;
            }

            boolean passwordValida = HashUtil.verificarPassword(password, usuario.getSalt(), usuario.getPassword());
            if (passwordValida) {
                logger.info("✅ Usuario autenticado correctamente: " + email);
                return usuario;
            } else {
                logger.warning("❌ Contraseña incorrecta para el usuario: " + email);
                return null;
            }
        } catch (Exception e) {
            logger.severe("Error al autenticar usuario: " + e.getMessage());
            return null;
        }
    }

    public boolean registrarUsuario(Usuario usuario) {
        try {
            if (correoExiste(usuario.getEmail())) {
                logger.warning("❌ El correo ya está registrado: " + usuario.getEmail());
                return false;
            }

            // Generar salt y hashear la contraseña usando HashUtil
            String salt = HashUtil.generarSalt();
            String hashedPassword = HashUtil.hashearConSalt(usuario.getPassword(), salt);
            usuario.setSalt(salt);
            usuario.setPassword(hashedPassword);

            boolean registrado = usuarioDAO.registrarUsuario(usuario);
            if (registrado) {
                logger.info("✅ Usuario registrado exitosamente: " + usuario.getEmail());
            } else {
                logger.warning("❌ Error al registrar usuario: " + usuario.getEmail());
            }
            return registrado;
        } catch (Exception e) {
            logger.severe("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean correoExiste(String email) {
        return usuarioDAO.correoExiste(email);
    }
}
