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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        String execCommand = String.format(parte + " \"db.archivo.find({PropietarioId: %s, Habilitado: true, PadreId:%s})\"", Id, PadreId);
        System.out.println(execCommand);
        return con.ejecutarComandoPowerShellJson(execCommand);
    }

    public JsonArray ObtenerArchivosAbuelo(String Id, String PadreId) {
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        //ejecución para buscar al papa
        String execCommand = String.format(parte + " \"db.archivo.find({PropietarioId: %s, Habilitado: true, _id:%s})\"", Id, PadreId);
        System.out.println(execCommand);
        //ejecución con el papa
        String ComandoFinal = String.format(parte + " \"db.archivo.find({PropietarioId: %s, Habilitado: true, PadreId:ObjectId('%s')})\"", Id, con.ejecutarComandoYObtenerPadreId(execCommand));
        return con.ejecutarComandoPowerShellJson(ComandoFinal);
    }

    public boolean CrearCarpeta(String Nombre, String PadreId, String CreadorId) {
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        // Comando para buscar la existencia de la carpeta
        String consulta = String.format("db.archivo.find({Nombre: '%s', PadreId: %s}).limit(1).count()", Nombre, PadreId);
        String execCommand = parte + " \"" + consulta + "\"";

        // Comprobar si ya existe
        if (con.ejecutarComandoMongoShellBoolean(execCommand, 1)) {
            return false;
        } else {
            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = fechaActual.format(formato);
            execCommand = String.format(parte + " \"db.archivo.insertOne({Nombre: '%s', Extension: 'carpeta', PadreId: %s, FechaCreacion: '%s', PropietarioId: %s, Habilitado: true})\"", Nombre, PadreId, fechaFormateada, CreadorId);
            System.out.println(execCommand);
            return con.ejecutarComandoMongoShellBoolean(execCommand, 0);
        }
    }

}
