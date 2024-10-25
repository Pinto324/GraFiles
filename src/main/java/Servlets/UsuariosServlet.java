/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DB.UsuariosDB;
import com.google.gson.Gson;
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
@WebServlet(name = "Usuario", urlPatterns = {"/Usuario/*"})
public class UsuariosServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final UsuariosDB DB = new UsuariosDB();

    //Get para usuarios
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        String accion = request.getParameter("accion");
        if (accion != null && accion.equals("login")) {
            String Usuario = request.getParameter("Usuario");
            String Password = request.getParameter("Password");
            String[] datos = DB.comprobarContraseña(Usuario, Password);
            if (datos[0].equals("Correcto")) {
                Document Info = new Document("username", datos[0])
                        .append("rol", datos[1]);
                String jsonEspecialidades = gson.toJson(Info);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonEspecialidades);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } else if (datos[0].equals("Incorrecto")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
