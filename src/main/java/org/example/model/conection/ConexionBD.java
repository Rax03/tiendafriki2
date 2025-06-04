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

            String driver = "org.mariadb.jdbc.Driver";

            Class.forName(driver);
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conexión establecida con éxito a la base de datos.");
        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public static Connection conectar() {
        try {
            if (conexion == null || conexion.isClosed()) {
                cargarConfiguracion();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error verificando conexión: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("✅ Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
        }
    }
}
