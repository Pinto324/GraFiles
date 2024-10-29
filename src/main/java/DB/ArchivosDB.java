/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

/**
 *
 * @author branp
 */
public class ArchivosDB {
    private Conexion con = new Conexion();
    
    public JsonArray ObtenerArchivosPropio(String Id, String PadreId) {
            String parte = "docker exec mongo-container mongosh GraFiles --eval";
            String execCommand = String.format(parte+" \"db.archivo.find({PropietarioId: %s, Habilitado: true, PadreId:%s})\"", Id,PadreId);
            System.out.println(execCommand);
            return con.ejecutarComandoPowerShellJson(execCommand);
    }
}
