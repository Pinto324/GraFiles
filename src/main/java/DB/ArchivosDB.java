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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        return con.ejecutarComandoPowerShellJson(execCommand);
    }
    
    public JsonArray ObtenerArchivosAdmin() {
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        String execCommand = String.format(parte + " \"db.archivo.find({ Habilitado: false})\"");
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
        String consulta = String.format("db.archivo.find({Nombre: '%s', PadreId: %s, Habilitado:true}).limit(1).count()", Nombre, PadreId);
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

    public boolean CrearTexto(String Nombre, String Extension,String PadreId, String CreadorId, String Contenido) {
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        String consulta = String.format("db.archivo.find({Nombre: '%s', PadreId: %s, Habilitado:true}).limit(1).count()", Nombre, PadreId);
        String execCommand = parte + " \"" + consulta + "\"";

        if (con.ejecutarComandoMongoShellBoolean(execCommand, 1)) {
            return false;
        } else {
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaFormateada = fechaHoraActual.format(formato);
            execCommand = String.format(parte + " \"db.archivo.insertOne({Nombre: '%s', Extension: '%s', PadreId: %s, FechaCreacion: '%s',  FechaModificacion: '%s',Contenido:'%s',PropietarioId: %s, Habilitado: true})\"", Nombre, Extension,PadreId, fechaFormateada, fechaFormateada, Contenido ,CreadorId);
            System.out.println(execCommand);
            return con.ejecutarComandoMongoShellBoolean(execCommand, 0);
        }
    }
    public boolean DesactivarArchivos(String id, Set<String> idsDesactivados) {
        if (idsDesactivados.contains(id)) {
            return true;
        }
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        String desactivarActualCommand = String.format(
                parte + " \"db.archivo.updateOne({_id: %s, Habilitado: true}, {$set: {Habilitado: false}})\"", id
        );
        boolean desactivadoActual = con.ejecutarComandoMongoShellBoolean(desactivarActualCommand, 0);

        if (!desactivadoActual) {
            return false;
        }

        idsDesactivados.add(id);

        String buscarHijosCommand = String.format(
                parte + " \"db.archivo.find({PadreId: %s, Habilitado: true}).toArray()\"", id
        );

        Set<String> hijosIds = new HashSet<>(con.obtenerIdsHijardos(buscarHijosCommand));

        for (String hijoId : hijosIds) {
            DesactivarArchivos(hijoId, idsDesactivados);
        }

        return true;
    }

    public boolean Mover(String Id, String IdPadre) {
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        String execCommand = String.format(
                parte + " \"db.archivo.updateOne({_id: %s, Habilitado: true}, {$set: {PadreId: %s}})\"",
                Id, IdPadre
        );
        System.out.println(execCommand);
        return con.ejecutarComandoMongoShellBoolean(execCommand, 0);
    }
   public boolean ActualizarTexto(String Id,String Nombre, String Extension,String Contenido, String PadreId) {
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        String consulta = String.format("db.archivo.find({Nombre: '%s', PadreId: %s, Habilitado:true}).limit(1).count()", Nombre, PadreId);
        String execCommand = parte + " \"" + consulta + "\"";

        if (con.ejecutarComandoMongoShellBoolean(execCommand, 1)) {
            return false;
        } else {
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaFormateada = fechaHoraActual.format(formato);
            execCommand = String.format(parte+" \"db.archivo.updateOne({_id: %s, Habilitado: true}, {$set: {Nombre:'%s',Extension:'%s',FechaModificacion:'%s',Contenido:'%s'}})\" ",Id, Nombre, Extension,fechaFormateada,Contenido);
            System.out.println(execCommand);
            return con.ejecutarComandoMongoShellBoolean(execCommand, 0);
        }
    }
    public boolean CompartirArchivo(String NombreU, String Nombre, String Extension, String idPadre, String FechaC, String FechaM, String Contenido) {
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        String consulta = String.format("db.usuario.find({Username: '%s'}).limit(1)", NombreU);
        String execCommand = parte + " \"" + consulta + "\"";
        System.out.println(execCommand);
        String id = con.ejecutarComandoYObtenerId(execCommand);
        System.out.println(id);
        if (id!=null) {
            execCommand = String.format(parte + " \"db.archivo.insertOne({Nombre: '%s', Extension: '%s', PadreId: %s, FechaCreacion: '%s',  FechaModificacion: '%s',Contenido:'%s',PropietarioId: %s, Habilitado: true})\"", Nombre, Extension,idPadre, FechaC, FechaM, Contenido ,id);
            System.out.println(execCommand);
            return con.ejecutarComandoMongoShellBoolean(execCommand, 0);
        } else {
            return false;
        }
    }
}
