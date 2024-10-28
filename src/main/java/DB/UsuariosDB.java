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

    private Conexion con = new Conexion();
    private final String URL = "mongodb://localhost:27017";

    public String[] comprobarContraseña(String usuario, String contra) {
        String[] Info = new String[3];
        try {
            // Comando de PowerShell para buscar el usuario en MongoDB
            System.out.println("Comando");
            String scriptPath = "C:/ruta/a/tu/script.js"; 
            String comando = String.format(
                    "docker exec -i mongodb-container mongosh %s",
                    scriptPath
            ); 
            String resultado = con.ejecutarComandoPowerShellString(comando);
            System.out.println("Resultado de la consulta: " + resultado);
            // Procesar la salida JSON
            if (resultado != null && !resultado.isEmpty()) {
                // Parsear el resultado JSON para obtener los datos
                String[] lines = resultado.split("\n");
                String json = lines[lines.length - 1]; // La última línea debe ser el JSON

                // Extraer el valor del hash de la contraseña y el rol del usuario
                // Asumiendo que el JSON es algo así: { "_id": "...", "Username": "...", "Password": "...", "Rol": "..." }
                String valorHashAlmacenado = ""; // Extraer del JSON
                String rol = ""; // Extraer del JSON

                // Utilizar una librería como org.json para parsear el JSON si es necesario
                // o usar simple string manipulation
                // Ejemplo de extracción (puedes reemplazar esto con una mejor forma de parseo JSON)
                if (json.contains("Password")) {
                    valorHashAlmacenado = json.split("\"Password\": \"")[1].split("\"")[0];
                    rol = json.split("\"Rol\": \"")[1].split("\"")[0];
                    System.out.println(rol);
                }
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] valorHashCalculado = digest.digest(contra.getBytes());
                String valorHashCalculadoHex = bytesToHex(valorHashCalculado);
                if (valorHashAlmacenado.equals(valorHashCalculadoHex)) {
                    Info[2] = usuario; // Username
                    Info[1] = rol; // Rol
                    Info[0] = "Correcto";
                    return Info;
                } else {
                    Info[0] = "Incorrecto";
                    return Info;
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
}
