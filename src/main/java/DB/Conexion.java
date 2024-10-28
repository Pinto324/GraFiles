/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.mongodb.client.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.bson.Document;

/**
 *
 * @author branp
 */
public class Conexion {

    public void ejecutarComandoMongoShell(String comando) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("mongo", "localhost:27017", "--eval", comando);
            processBuilder.redirectErrorStream(true); // Combina salida de error y salida estándar

            Process proceso = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = proceso.waitFor();
            System.out.println("Comando ejecutado con código de salida: " + exitCode);

        } catch (Exception e) {
            System.out.println("Error al ejecutar el comando: " + e.getMessage());
        }
    }

    public String ejecutarComandoPowerShellString(String comando) {
        StringBuilder output = new StringBuilder();
        try {
            // Comando que ejecutará el contenedor de MongoDB con la consulta deseada
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c",comando);
            processBuilder.redirectErrorStream(true); // Combina salida de error y salida estándar

            Process proceso = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = proceso.waitFor();
            if (exitCode != 0) {
                System.out.println("Error al ejecutar el comando. Código de salida: " + exitCode);
            }

        } catch (Exception e) {
            System.out.println("Error al ejecutar el comando: " + e.getMessage());
        }
        return output.toString(); // Devuelve la salida del comando
    }

}
