/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bson.Document;

/**
 *
 * @author branp
 */
public class ArchivosDB {
    private Conexion con = new Conexion();

    public String[] ObtenerArchivosPropio(String usuario, String contra) {
        
        Document Usuario = null;
        String[] Info = new String[3];
        return null;
    }
}
