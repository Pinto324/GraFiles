/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DB.UsuariosDB;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

/**
 *
 * @author branp
 */
@WebServlet(name = "Usuario", urlPatterns = {"/UsuarioServlet"})
public class UsuariosServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final UsuariosDB DB = new UsuariosDB();

    //Get para usuarios
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Permitir solicitudes CORS desde el frontend en localhost
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json"); // Asegura que el tipo de contenido sea JSON

        // Recibir parámetro de acción y verificar login
        String accion = request.getParameter("accion");
        if (accion != null && accion.equals("login")) {
            String Usuario = request.getParameter("Usuario");
            String Password = request.getParameter("Password");

            // Llamar al método para verificar el usuario y contraseña
            String[] datos = DB.comprobarContraseña(Usuario, Password);
            JsonObject responseData = new JsonObject();

            // Responder según el resultado de la verificación
            if ("Correcto".equals(datos[0])) {
                responseData.addProperty("valid", true);
                responseData.addProperty("id", datos[3]);
                responseData.addProperty("username", datos[2]);
                responseData.addProperty("tipo", datos[1]);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                responseData.addProperty("valid", false);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            // Escribir el JSON de respuesta y cerrar la salida
            PrintWriter out = response.getWriter();
            out.write(responseData.toString());
            out.flush();
        }else if (accion != null && accion.equals("CrearUsuario")) {
            String Usuario = request.getParameter("Usuario");
            String Password = request.getParameter("Password");
            String name = request.getParameter("Nombre");
            String[] datos = DB.comprobarContraseña(Usuario, Password);
            if (DB.CrearUsuario(Usuario, Password, name)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
