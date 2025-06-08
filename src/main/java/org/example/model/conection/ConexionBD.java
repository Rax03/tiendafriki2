package org.example.model.conection;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConexionBD {

    private static final EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("tiendafriki"); // "nombrePU" debe coincidir con persistence.xml
            System.out.println("✅ EntityManagerFactory inicializado con éxito.");
        } catch (Throwable ex) {
            System.err.println("❌ Error al inicializar EntityManagerFactory: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✅ EntityManagerFactory cerrado correctamente.");
        }
    }
}
