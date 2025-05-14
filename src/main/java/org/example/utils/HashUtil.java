package org.example.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashUtil {

    /**
     * Genera un salt aleatorio usando SecureRandom.
     *
     * @return El salt codificado en Base64.
     */
    public static String generarSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashea una contraseña utilizando un salt. El hash se realiza con SHA-256.
     *
     * @param password La contraseña a hashear.
     * @param salt El salt utilizado para el hash.
     * @return El hash codificado en Base64.
     */
    public static String hashearConSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashed = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear", e);
        }
    }

    /**
     * Verifica si una contraseña ingresada coincide con un hash almacenado.
     *
     * @param passwordIngresada La contraseña ingresada por el usuario.
     * @param salt El salt utilizado para el hash.
     * @param hashAlmacenado El hash almacenado en la base de datos.
     * @return {@code true} si la contraseña coincide, {@code false} si no.
     */
    public static boolean verificarPassword(String passwordIngresada, String salt, String hashAlmacenado) {
        String hashCalculado = hashearConSalt(passwordIngresada, salt);
        return hashCalculado.equals(hashAlmacenado);
    }

    /**
     * Valida si un email tiene un formato adecuado (correo electrónico válido).
     * Se usa un patrón regex para validación.
     *
     * @param email El email que se desea validar.
     * @return {@code true} si el correo es válido, {@code false} de lo contrario.
     */
    public static boolean validarEmail(String email) {
        // Expresión regular para validar emails.
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
