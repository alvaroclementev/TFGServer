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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servicios.GestorRestaurante;

/**
 *
 * @author Alvaro
 */
public class ControllerPedidos extends HttpServlet {

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
        try{
            PrintWriter out = response.getWriter();
            String opcion = request.getParameter("opcion");
            switch(opcion){
                case "mostrarProductos":
                        String selectedRestauranteString = request.getParameter("selectedRestauranteId");
                        if(selectedRestauranteString == null){
                            out.write("Error: no se ha encontrado el restaurante en la request");
                            System.out.println("Error: no se ha encontrado el restaurante en la request");
                        }
                        else{
                            int selectedRestauranteId = Integer.parseInt(selectedRestauranteString);
                            Restaurante selectedRestaurante = GestorRestaurante.findRestaurante(selectedRestauranteId, request);
                            if(selectedRestaurante == null){
                                out.write("Error: no se ha encontrado el restaurante");
                                System.out.println("Error: no se ha encontrado el restaurante");
                            }
                            else{
                                String result = selectedRestaurante.mostrarProductos();
                                out.write(result);
                                System.out.println(result);
                            }
                        }
                    break;
                    
                
                case "confirmarPedido":
                    selectedRestauranteString = request.getParameter("selectedRestauranteId");
                    if(selectedRestauranteString == null){
                        out.write("Error: no se ha encontrado el restaurante en la request");
                        System.out.println("Error: no se ha encontrado el restaurante en la request");
                    }
                    else{
                        int selectedRestauranteId = Integer.parseInt(selectedRestauranteString);
                        Restaurante selectedRestaurante = GestorRestaurante.findRestaurante(selectedRestauranteId, request);
                        if(selectedRestaurante == null){
                            out.write("Error: no se ha encontrado el restaurante");
                            System.out.println("Error: no se ha encontrado el restaurante");
                        }
                        else{
                            String message=selectedRestaurante.hacerPedido(request);
                            System.out.println(message);
                            out.write(message);
                        }
                    }
                    break;
                
                case "checkout":
                    selectedRestauranteString = request.getParameter("selectedRestauranteId");
                    if(selectedRestauranteString == null){
                        out.write("Error: no se ha encontrado el restaurante en la request");
                        System.out.println("Error: no se ha encontrado el restaurante en la request");
                    }
                    else{
                        int selectedRestauranteId = Integer.parseInt(selectedRestauranteString);
                        Restaurante selectedRestaurante = GestorRestaurante.findRestaurante(selectedRestauranteId, request);
                        if(selectedRestaurante == null){
                            out.write("Error: no se ha encontrado el restaurante");
                            System.out.println("Error: no se ha encontrado el restaurante");
                        }
                        else{
                            String message=selectedRestaurante.checkout(request);
                            System.out.println(message);
                            out.write(message);
                        }
                    }
                    
                    break;
                
                case "pedidoParaReserva":
                    //Se necesita selectedRestauranteId, reservaId, productos y comentarios
                    selectedRestauranteString = request.getParameter("selectedRestauranteId");
                    if(selectedRestauranteString == null){
                        out.write("Error: no se ha encontrado el restaurante en la request");
                        System.out.println("Error: no se ha encontrado el restaurante en la request");
                    }
                    else{
                        int selectedRestauranteId = Integer.parseInt(selectedRestauranteString);
                        Restaurante selectedRestaurante = GestorRestaurante.findRestaurante(selectedRestauranteId, request);
                        if(selectedRestaurante == null){
                            out.write("Error: no se ha encontrado el restaurante");
                            System.out.println("Error: no se ha encontrado el restaurante");
                        }
                        else{
                            String message=selectedRestaurante.pedidoParaReserva(request);
                            System.out.println(message);
                            out.write(message);
                        }
                    }
                    
                    break;
                    
                default:
                    out.write("Error: opcion desconocida");
                    System.out.println("Error: opcion desconocida");
                    
                    break;                
            }
                
            
        } catch(IOException e){
            System.out.println("IOException");
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
