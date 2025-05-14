package org.example.model.service;

import org.example.model.entity.Usuario;

public class LoginService {
    private final UsuarioService usuarioService;

    // Constructor recibe UsuarioService, no solo UsuarioDAO
    public LoginService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Método para autenticar un usuario según su correo y contraseña.
     * @param email Correo del usuario.
     * @param contraseña Contraseña del usuario.
     * @return Usuario autenticado o null si la autenticación falla.
     */
    public Usuario autenticarUsuario(String email, String contraseña) {
        // Se podría agregar validación extra si es necesario antes de delegar
        if (email == null || email.isEmpty() || contraseña == null || contraseña.isEmpty()) {
            return null; // O lanzar excepción personalizada
        }

        return usuarioService.autenticarUsuario(email, contraseña);
    }
}
