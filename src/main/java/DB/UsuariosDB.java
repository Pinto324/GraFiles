/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author branp
 */
public class UsuariosDB {
    private Conexion con;
    
    public String[] comprobarContraseña(String usuario, String contra) {
        Document Usuario = null;
        String[] Info = new String[3];
        try {
            con.iniciarConexion();           
            MongoCollection<Document> collection = con.getDatabase().getCollection("Usuarios");
            Usuario = collection.find(eq("Username", usuario)).first();
            if (Usuario != null) {
                String valorHashAlmacenado = Usuario.getString("Password");
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] valorHashCalculado = digest.digest(contra.getBytes());
                String valorHashCalculadoHex = bytesToHex(valorHashCalculado);
                if(valorHashAlmacenado.equals(valorHashCalculadoHex)){  
                    Info[2] = Usuario.getString("Username");
                    Info[1] = Usuario.getString("Rol");
                    Info[0] = "Correcto";
                    return Info;
                }else{
                    Info[0] = "Incorrecto";
                    con.cerrarConexion();
                    return Info;
                }
            }
            con.cerrarConexion();
            Info[0] = "Nulo";
            return null;
        } catch (NoSuchAlgorithmException e){
        } finally {
            con.cerrarConexion();
            Info[0] = "Fallo";
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
