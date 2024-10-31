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
import java.util.HashSet;
import java.util.Set;
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
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if (accion != null && accion.equals("cargarArchivos")) {
            String Id = request.getParameter("Id");
            String PadreId = request.getParameter("Padre");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JsonArray jsonObject = DB.ObtenerArchivosPropio(Id, PadreId);
            if (jsonObject == null) {
                // Enviar un JSON vacío o un mensaje de error
                out.print("{\"error\": \"No se encontraron resultados\"}");
            } else {
                String jsonOutput = jsonObject.toString();
                out.print(jsonOutput);
            }
            out.flush();
        } else if (accion != null && accion.equals("retroceder")) {
            String Id = request.getParameter("Id");
            String PadreId = request.getParameter("Padre");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JsonArray jsonObject = DB.ObtenerArchivosAbuelo(Id, PadreId);
            if (jsonObject == null) {
                // Enviar un JSON vacío o un mensaje de error
                out.print("{\"error\": \"No se encontraron resultados\"}");
            } else {
                String jsonOutput = jsonObject.toString();
                out.print(jsonOutput);
            }
            out.flush();
        }else  if (accion != null && accion.equals("cargarArchivosAdmin")) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JsonArray jsonObject = DB.ObtenerArchivosAdmin();
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Permitir solicitudes CORS desde el frontend en localhost
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json"); // Asegura que el tipo de contenido sea JSON

        // Recibir parámetro de acción y verificar login
        String accion = request.getParameter("accion");
        if (accion != null && accion.equals("IngresarCarpeta")) {
            String NombreCarpeta = request.getParameter("NombreCarpeta");
            String IdPadre = request.getParameter("IdPadre");
            String IdCreador = request.getParameter("IdCreador");
            if (DB.CrearCarpeta(NombreCarpeta, IdPadre, IdCreador)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else if (accion != null && accion.equals("CrearTexto")) {
            String Nombre = request.getParameter("Nombre");
            String IdPadre = request.getParameter("IdPadre");
            String IdCreador = request.getParameter("IdCreador");
            String Contenido = request.getParameter("Contenido");
            String Extension = request.getParameter("Extension");
            if (DB.CrearTexto(Nombre, Extension,IdPadre, IdCreador,Contenido)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else if (accion != null && accion.equals("ActualizarTexto")) {
            String id = request.getParameter("id");
            String Nombre = request.getParameter("Nombre");
            String Contenido = request.getParameter("Contenido");
            String Extension = request.getParameter("Extension");
            String IdPadre = request.getParameter("IdPadre");
            if (DB.ActualizarTexto(id,Nombre, Extension, Contenido,IdPadre)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else if (accion != null && accion.equals("CompartirArchivo")) {
            String NombreDeUsuario = request.getParameter("NombreDeUsuario");
            String IdPadre = request.getParameter("IdPadre");
            String IdCreador = request.getParameter("IdObjeto");
            String Nombre = request.getParameter("Nombre");
            String Extension = request.getParameter("Extension");
            String FechaM = request.getParameter("FechaM");
            String FechaC = request.getParameter("FechaC");
            String Contenido = request.getParameter("Contenido");
            String Habilitado = request.getParameter("Habilitado");
            if (DB.CompartirArchivo(NombreDeUsuario,Nombre, Extension,IdPadre,FechaC ,FechaM,Contenido)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, OPTIONS"); // Agregar PUT aquí
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        // Leer los parámetros del cuerpo de la solicitud
        String accion = request.getParameter("accion");
        System.out.println("Entro en doPut con acción: " + accion);

        if (accion != null && accion.equals("Desactivar")) {
            String Id = request.getParameter("IdArchivo");
            Set<String> idsDesactivados = new HashSet<>();
            if (Id != null && DB.DesactivarArchivos(Id, idsDesactivados)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (accion != null && accion.equals("MoverCarpeta")) {
            String Id = request.getParameter("IdArchivo");
            String IdPadreNuevo = request.getParameter("IdMover");
            if (Id != null && DB.Mover(Id, IdPadreNuevo)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
