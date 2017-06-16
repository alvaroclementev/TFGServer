/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import servicios.GestorMesa;
import servicios.GestorRestaurante;

/**
 *
 * @author Alvaro
 */
public class ControllerMesa extends HttpServlet {

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
                case "mostrar":
                    HttpSession session = request.getSession();
                    GestorMesa selectedRestaurante = (GestorMesa) session.getAttribute("selectedRestaurante");
                    if(selectedRestaurante == null){
                        System.out.println("Se ha entrado aqui");
                        //Se mira si la ha pasado en la request, que significa que
                        //Solo esta mirando
                        if(request.getParameter("selectedRestauranteId") == null){
                            out.write("Error: no hay un restaurante seleccionado");
                        }
                        else{
                            int id = Integer.parseInt(request.getParameter("selectedRestauranteId"));
                            selectedRestaurante = GestorRestaurante.findRestaurante(id, request);
                            if(selectedRestaurante == null){
                                System.out.println("Error: no se ha encontrado el restaurante con id = " + id);
                                out.write("Error: no se ha encontrado el restaurante con id = " + id);
                            }
                            else{
                                String result = selectedRestaurante.mostrarMesas();
                                System.out.println(result);
                                out.write(result);
                            }
                        }
                    }
                    else{
                        String result = selectedRestaurante.mostrarMesas();
                        System.out.println(result);
                        out.write(result);
                    }
                    break;
                case "reservar":
                    try{
                        String restauranteString = request.getParameter("selectedRestauranteId");
                        if(restauranteString == null){
                            out.write("Error: no hay un restaurante seleccionado");
                        }
                        else{
                            selectedRestaurante = GestorRestaurante.findRestaurante(Integer.parseInt(restauranteString), request);
                            if(selectedRestaurante == null){
                                out.write("Error: no se ha encontrado el restaurante seleccionado");
                            }
                            else{
                                String result = selectedRestaurante.reservarMesa(request);
                                System.out.println(result);
                                out.write(result);
                            }
                            
                            
                        }
                                                
                        
                    } catch(NumberFormatException nfe){
                        System.out.println("El id no es un int parseable\n"+ nfe);
                        out.write("Error: El id no es un int parseable");
                    }

                    break;
                case "sentarse":
                    try{
                        String selectedRestauranteId = request.getParameter("selectedRestauranteId");
                        if(selectedRestauranteId == null){
                            out.write("Error: no hay un restaurante seleccionado");
                        }
                        else{
                            selectedRestaurante = GestorRestaurante.findRestaurante(Integer.parseInt(selectedRestauranteId), request);
                            if(selectedRestaurante == null){
                                out.write("Error: no se ha encontrado el restaurante seleccionado");
                            }
                            else{
                                String result;
                                if(request.getParameter("reservada") != null){
                                    result = selectedRestaurante.sentarEnReservada(request);
                                }
                                else{
                                    result = selectedRestaurante.sentarEnMesa(request);
                                }
                                
                                System.out.println(result);
                                out.write(result);
                            }
                        }
                        
                    } catch(NumberFormatException nfe){
                        System.out.println("El id no es un int parseable\n"+ nfe);
                        out.write("Error: El id no es un int parseable");
                    }
                    break;
                    
                case "levantarse":
                    //TODO: hacer los checks necesarios
                    try{
                        String selectedRestauranteId = request.getParameter("selectedRestauranteId");
                        if(selectedRestauranteId == null){
                            out.write("Error: no hay un restaurante seleccionado");
                        }
                        else{
                            selectedRestaurante = GestorRestaurante.findRestaurante(Integer.parseInt(selectedRestauranteId), request);
                            if(selectedRestaurante == null){
                                out.write("Error: no se ha encontrado el restaurante seleccionado");
                            }
                            else{
                                String result = selectedRestaurante.levantarDeMesa(request);
                                System.out.println(result);
                                out.write(result);
                            }
                        }
                        
                    } catch(NumberFormatException nfe){
                        System.out.println("Error: El id no es un int parseable\n"+ nfe);
                        out.write("Error: El id no es un int parseable");
                    }
                    break;
                    
                    
                case "mostrarHorasDisponibles":
                    //Devuelve un JSONArray con las horas disponibles en forma de String
                    try{
                        String selectedRestauranteId = request.getParameter("selectedRestauranteId");
                        if(selectedRestauranteId == null){
                            out.write("Error: no hay un restaurante seleccionado");
                        }
                        else{
                            selectedRestaurante = GestorRestaurante.findRestaurante(Integer.parseInt(selectedRestauranteId), request);
                            if(selectedRestaurante == null){
                                out.write("Error: no se ha encontrado el restaurante seleccionado");
                            }
                            else{
                                String result = selectedRestaurante.mostrarHorasDisponibles(request);
                                System.out.println("Las response es:\n"+result);
                                out.write(result);
                            }
                        }
                        
                    } catch(NumberFormatException nfe){
                        System.out.println("Error: El id no es un int parseable\n"+ nfe);
                        out.write("Error: El id no es un int parseable");
                    }
                    break;
                    
                case "cancelarReserva":
                    //Necesita restauranteId y reservaId
                    try{
                        String selectedRestauranteId = request.getParameter("selectedRestauranteId");
                        if(selectedRestauranteId == null){
                            out.write("Error: no hay un restaurante seleccionado");
                        }
                        else{
                            selectedRestaurante = GestorRestaurante.findRestaurante(Integer.parseInt(selectedRestauranteId), request);
                            if(selectedRestaurante == null){
                                out.write("Error: no se ha encontrado el restaurante seleccionado");
                            }
                            else{
                                String result = selectedRestaurante.cancelarReserva(request);
                                System.out.println("Las response es:\n"+result);
                                out.write(result);
                            }
                        }
                        
                    } catch(NumberFormatException nfe){
                        System.out.println("Error: El id no es un int parseable\n"+ nfe);
                        out.write("Error: El id no es un int parseable");
                    }

                    break;
                default:
                    System.out.println("Error: Opcion desconocida");
                    out.write("Error: Opcion desconocida");
                    break;
            }
            
            out.close();
        } catch(IOException e){
            System.out.println(e);
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
