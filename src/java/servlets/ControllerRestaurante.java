/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dominio.Horario;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servicios.GestorRestaurante;

/**
 *
 * @author Alvaro
 */
public class ControllerRestaurante extends HttpServlet {

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
        //Inicializa un horario por defecto (Testing) si no esta hecho ya
        ServletContext contextoApp = getServletContext();
        Horario defaultHorario = (Horario) contextoApp.getAttribute("defaultHorario");
        if(defaultHorario == null){
            LocalDate hoy = LocalDate.now();
            LocalTime horaInicio = LocalTime.of(8, 30);
            LocalTime horaFin = LocalTime.of(23, 30);
            LocalDateTime defaultInicio = LocalDateTime.of(hoy, horaInicio);
            LocalDateTime defaultFin = LocalDateTime.of(hoy, horaFin);
            defaultHorario = new Horario(defaultInicio, defaultFin, 10);
            
            contextoApp.setAttribute("defaultHorario", defaultHorario);
        }
        
        try (PrintWriter out = response.getWriter()) {
            String opcion = request.getParameter("opcion");
            String json;
            switch(opcion){
            case "mostrar":
                
                json = GestorRestaurante.mostrarRestaurantes(contextoApp);
                
                out.write(json);
                out.close();
                
                break;

            case "seleccionar":
                //Solo tiene sentido cuando ya se ha sentado el cliente, cuando solo esta mirando no
                //Parametros: id(int)
                try{
                    int id = Integer.parseInt(request.getParameter("id"));
                    json = GestorRestaurante.seleccionarRestaurante(id, request);

                    out.write(json);
                }
                catch(NumberFormatException nfe){
                    System.out.println(nfe);
                    out.write("No se ha podido parsear la id");
                }
                out.close();
                
                break;
                                
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
