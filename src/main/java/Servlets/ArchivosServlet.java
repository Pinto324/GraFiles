/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DB.ArchivosDB;
import DB.UsuariosDB;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author branp
 */
@WebServlet(name = "Archivos", urlPatterns = {"/ArchivosServlet"})
public class ArchivosServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final ArchivosDB DB = new ArchivosDB();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        String accion = request.getParameter("accion");
        if (accion != null && accion.equals("cargarArchivos")) {
            String Id = request.getParameter("Id");
            String PadreId = request.getParameter("Padre");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JsonArray jsonObject = DB.ObtenerArchivosPropio(Id,PadreId);
            if (jsonObject == null) {
                // Enviar un JSON vacío o un mensaje de error
                out.print("{\"error\": \"No se encontraron resultados\"}");
            } else {
                String jsonOutput = jsonObject.toString();
                out.print(jsonOutput);
            }
            out.flush();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
