package org.example.model.conection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static Connection conexion = null;

    static {
        cargarConfiguracion();
    }

    private static void cargarConfiguracion() {
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            Properties config = new Properties();
            config.load(fis);

            String url = config.getProperty("db.url");
            String user = config.getProperty("db.user");
            String password = config.getProperty("db.password");
            String driver = config.getProperty("db.driver");

            Class.forName(driver);
            conexion = DriverManager.getConnection(url, user, password);

        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public static Connection conectar() {
        try {
            if (conexion == null || conexion.isClosed()) { // ✅ Verifica si la conexión fue cerrada
                cargarConfiguracion();
            }
        } catch (SQLException e) {
            System.err.println("Error verificando conexión: " + e.getMessage());
        }
        return conexion;
    }


    public static void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
