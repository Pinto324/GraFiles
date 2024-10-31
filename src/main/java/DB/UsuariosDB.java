/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author branp
 */
public class UsuariosDB {

    private Conexion con = new Conexion();
    private final String scriptsUrl = "docker exec mongo-container mongosh GraFiles --eval";;

    public String[] comprobarContraseña(String usuario, String contra) {
        String[] Info = new String[4];
        try {
            String execCommand = String.format(scriptsUrl+" \"db.usuario.findOne({ Username: '%s' })\"", usuario);
            String resultado = con.ejecutarComandoPowerShellString(execCommand);
            if (resultado != null && !resultado.isEmpty()) {
                String jsonResult = resultado.trim();
                if (jsonResult.startsWith("{") && jsonResult.endsWith("}")) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(jsonResult, JsonObject.class);
                    String valorHashAlmacenado = jsonObject.getAsJsonPrimitive("Password").getAsString();
                    String rol = jsonObject.getAsJsonPrimitive("Rol").getAsString();
                    String Id = jsonObject.getAsJsonPrimitive("_id").getAsString();
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] valorHashCalculado = digest.digest(contra.getBytes());
                    String valorHashCalculadoHex = bytesToHex(valorHashCalculado);
                    if (valorHashAlmacenado.equals(valorHashCalculadoHex)) {
                        Info[3] = Id;
                        Info[2] = usuario; // Username
                        Info[1] = rol; // Rol
                        Info[0] = "Correcto";
                        return Info;
                    } else {
                        Info[0] = "Incorrecto";
                        return Info;
                    }
                } else {
                    System.out.println("El resultado no es un JSON válido: " + resultado);
                }
            } else {
                Info[0] = "Nulo";
                return Info;
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error en el algoritmo de hash: " + e.getMessage());
            Info[0] = "Fallo";
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            Info[0] = "Fallo";
        }
        return Info;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
    
    public boolean CrearUsuario(String User, String password, String name) {
        String parte = "docker exec mongo-container mongosh GraFiles --eval";
        String consulta = String.format("db.usuario.find({Username: '%s'}).limit(1).count()", User);
        String execCommand = parte + " \"" + consulta + "\"";
        if (con.ejecutarComandoMongoShellBoolean(execCommand, 1)) {
            return false;
        } else {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UsuariosDB.class.getName()).log(Level.SEVERE, null, ex);
            }
                    byte[] valorHashCalculado = digest.digest(password.getBytes());
                    String valorHashCalculadoHex = bytesToHex(valorHashCalculado);
            execCommand = String.format(parte + " \"db.usuario.insertOne({Username: '%s',  Password: '%s', Nombre: '%s', Rol: 'Empleado'})\"", User, valorHashCalculadoHex, name);
            System.out.println(execCommand);
            return con.ejecutarComandoMongoShellBoolean(execCommand, 0);
        }
    }
}
