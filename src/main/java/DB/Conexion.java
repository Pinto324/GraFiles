/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", comando);
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

        // Reemplaza "(*XDXD)" por saltos de línea antes de devolver el resultado
        return output.toString().replace("(*XDXD)", "\n");
    }

    public JsonArray ejecutarComandoPowerShellJson(String comando) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", comando);
            processBuilder.redirectErrorStream(true);

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

        // Reemplaza `(*XDXD)` por saltos de línea
        String outputString = output.toString().replace("(*XDXD)", "\n");

        JsonArray jsonArray = null;
        try {
            Gson gson = new Gson();
            jsonArray = gson.fromJson(outputString, JsonArray.class);

        } catch (Exception e) {
            System.out.println("Error al convertir la salida en JSON Array: " + e.getMessage());
        }
        return jsonArray; // Devuelve el JsonArray con la salida del comando
    }

    public String ejecutarComandoYObtenerPadreId(String comando) {
        StringBuilder output = new StringBuilder();
        String padreId = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", comando);
            processBuilder.redirectErrorStream(true);
            Process proceso = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (line.contains("PadreId")) {
                    int start = line.indexOf("ObjectId('") + 10;
                    int end = line.indexOf("')", start);
                    padreId = line.substring(start, end);
                    break;
                }
            }

            int exitCode = proceso.waitFor();
            if (exitCode != 0) {
                System.out.println("Error al ejecutar el comando. Código de salida: " + exitCode);
            }

        } catch (Exception e) {
            System.out.println("Error al ejecutar el comando: " + e.getMessage());
        }
        System.out.println(padreId);
        return padreId;
    }
    
    public String ejecutarComandoYObtenerId(String comando) {
        StringBuilder output = new StringBuilder();
        String padreId = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", comando);
            processBuilder.redirectErrorStream(true);
            Process proceso = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (line.contains("_id")) {
                    int start = line.indexOf("ObjectId('");
                    int end = line.indexOf("')", start);
                    padreId = line.substring(start, end+2);
                    break;
                }
            }

            int exitCode = proceso.waitFor();
            if (exitCode != 0) {
                System.out.println("Error al ejecutar el comando. Código de salida: " + exitCode);
            }

        } catch (Exception e) {
            System.out.println("Error al ejecutar el comando: " + e.getMessage());
        }
        System.out.println(padreId);
        return padreId;
    }

    public boolean ejecutarComandoMongoShellBoolean(String comando, int caso) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", comando);
            processBuilder.redirectErrorStream(true); // Combina salida de error y salida estándar

            Process proceso = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = proceso.waitFor();
            System.out.println("Comando ejecutado con código de salida: " + exitCode);

            // Devuelve true si el código de salida es 0 (éxito), false en caso contrario
            switch (caso) {
                //crear archivo
                case 0:
                    return exitCode == 0;
                //Verificar que no exista archivo con mismo nombre
                case 1:
                    if (exitCode == 0) {
                        // Procesar la salida para obtener el conteo
                        String outputStr = output.toString().trim();
                        int contador = Integer.parseInt(outputStr);
                        return contador > 0; // Retorna true si hay al menos un documento encontrado
                    } else {
                        System.out.println("El comando no se ejecutó correctamente.");
                        return false; // Retorna false si hay un error en la ejecución
                    }
                default:
                    return false;
            }

        } catch (Exception e) {
            System.out.println("Error al ejecutar el comando: " + e.getMessage());
            return true; // Devuelve false en caso de excepción
        }
    }

    public Set<String> obtenerIdsHijardos(String comando) {
        Set<String> idsHijos = new HashSet<>();
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", comando);
            processBuilder.redirectErrorStream(true);
            Process proceso = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (line.contains("ObjectId(")) {
                    int start = line.indexOf("ObjectId('") + 10;
                    int end = line.indexOf("')", start);

                    // Verifica que las posiciones sean válidas
                    if (start > 9 && end > start) {
                        String hijoId = line.substring(start, end);
                        idsHijos.add("ObjectId('" + hijoId + "')");
                        System.out.println("ObjectId('" + hijoId + "')");
                    }
                }
            }

            int exitCode = proceso.waitFor();
            if (exitCode != 0) {
                System.out.println("Error al ejecutar el comando. Código de salida: " + exitCode);
            }

        } catch (Exception e) {
            System.out.println("Error al ejecutar el comando: " + e.getMessage());
        }

        System.out.println("Ids de hijos encontrados: " + idsHijos);
        return idsHijos;
    }

}
