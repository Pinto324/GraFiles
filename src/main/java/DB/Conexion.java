/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.mongodb.client.*;

/**
 *
 * @author branp
 */
public class Conexion {
    private final String URL = "mongodb://localhost:27017";
    private MongoClient Cliente;
    private MongoDatabase Database;
     public Conexion() {
        iniciarConexion();
    }

    public void iniciarConexion() {
        try {
            Cliente = MongoClients.create(URL);
            Database = Cliente.getDatabase("Clinica_Medica");
            System.out.println("Conexión exitosa a MongoDB");
        } catch (Exception e) {
            System.out.println("Error al abrir conexión: " + e.getMessage());
        }
    }

    public MongoDatabase getDatabase() {
        return Database;
    }

    public void cerrarConexion() {
        if (Cliente != null) {
            Cliente.close();
        }
    }
    
}
