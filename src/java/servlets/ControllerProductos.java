/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dao.ProductoDAO;
import dominio.Producto;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servicios.GestorProducto;

/**
 *
 * @author Alvaro
 */
public class ControllerProductos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String vista = null;
        String opcion = request.getParameter("opcion");
        ProductoDAO dao;
        String json;
        GestorProducto gestor = new GestorProducto(this, request, response);        
        switch(opcion){
            case "buscar":
                json = gestor.buscarProducto();
                try (PrintWriter out = response.getWriter()) {
                    out.println(json);
                    out.close();
                }
                break;

            case "mostrar":
                json = gestor.mostrarProductos();
                try (PrintWriter out = response.getWriter()) {
                    out.println(json);
                    out.close();
                }
                break;

            case "buscarNoJSON":
                vista= gestor.buscarProductoNoJSON();
                break;

            case "NoJSON":
                vista= gestor.mostrarProductosNoJSON();
                break;
        }        
        
        if(vista != null)
            request.getRequestDispatcher(vista).forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
