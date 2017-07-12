/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dominio.Restaurante;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servicios.GestorRestaurante;

/**
 *
 * @author Alvaro
 */
@WebServlet(name = "ControllerPanelControl", urlPatterns = {"/PaneldeControl"})
public class ControllerPanelControl extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            String selectedRestauranteString = request.getParameter("selectedRestauranteId");
            int selectedRestauranteId = Integer.parseInt(selectedRestauranteString);
            
            Restaurante restaurante = GestorRestaurante.findRestaurante(selectedRestauranteId, request);
            request.setAttribute("restaurante", restaurante);
            
            String opcion= request.getParameter("opcion");
            if(opcion == null){
                opcion = "";
            }
            switch(opcion){                
                case "actualizar":
                    
                    //Primero coger las mesas
                    String mesas = restaurante.mostrarMesas();
                    
                    //Coger las reservas
                    String reservas = restaurante.mostrarReservas();
                    //Coger las activas
                    String reservasActivas = restaurante.mostrarReservasActivas();
                    
                    //Coger los pedidos
                    String productos = restaurante.mostrarPedidos();
                    
                    //Coger solicitudes
                    String solicitudes = restaurante.mostrarSolicitudesCamarero();
                    
                    String respuesta = '[' + mesas + ", " + reservas + ", " + reservasActivas + ", " + productos +  ", " + solicitudes + ']';
                    out.write(respuesta);
                    break;
                
                case "":
                    //Mostrar
                    request.getRequestDispatcher("panelcontrol.jsp").forward(request, response);           
            }
           
        }
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
