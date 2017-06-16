/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import com.google.gson.Gson;
import dao.RestauranteDAO;
import dominio.Horario;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import servlets.ControllerProductos;

/**
 *
 * @author Alvaro
 */
public class GestorRestaurante {

    private static GestorRestaurante instance;
    
    public static GestorRestaurante getInstance() {
        if(instance == null){
            instance = new GestorRestaurante();
        }
        return instance;
    }
    
    private GestorRestaurante(){}
    
    public static String mostrarRestaurantes(ServletContext contextoApp){
        Gson gson = new Gson();
        String res = "No se han encontrado restaurantes";
        try {     
            //Lista de Restaurantes deberia estar cacheada
            HashMap<Integer, GestorMesa> restaurantesMap = (HashMap) contextoApp.getAttribute("restaurantesMap");
            if(restaurantesMap == null){
                restaurantesMap = new HashMap();
                RestauranteDAO dao = new RestauranteDAO();
                TreeSet<GestorMesa> set = (TreeSet) dao.findAllRestaurantes();
                
                Horario defaultHorario = (Horario) contextoApp.getAttribute("defaultHorario");
                System.out.println("Inicializando Horario por defecto con horario =  " + defaultHorario);
                for(GestorMesa restaurante : set){
                    //FIXME: Por ahora inicializamos el horario con el por defecto
                    //Se pasa una copia del horario, para que sea independiente en los distintos restaurantes
                    restaurante.setHorario(new Horario(defaultHorario));
                    restaurantesMap.put(restaurante.getId(), restaurante);
                }
                contextoApp.setAttribute("restaurantesMap", restaurantesMap);
            }
            Collection<GestorMesa> values = restaurantesMap.values();
            if(values.isEmpty()){
                System.out.println("Error: No se ha encontrado ningun restaurante");
            }
            else{
                res = gson.toJson(values);
                //System.out.println(res);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return res;
    }
    
    public static String seleccionarRestaurante(int id, HttpServletRequest request){
        Gson gson = new Gson();
        String res = "No se han encontrado el restaurante";
        HttpSession sesion = request.getSession();
        ServletContext contextoApp = sesion.getServletContext();
        try {     
            //Lista de Restaurantes deberia estar cacheada
            HashMap<Integer, GestorMesa> restaurantesMap = (HashMap) contextoApp.getAttribute("restaurantesMap");
            if(restaurantesMap == null){
                restaurantesMap = new HashMap();
                RestauranteDAO dao = new RestauranteDAO();
                TreeSet<GestorMesa> set = (TreeSet) dao.findAllRestaurantes();
                for(GestorMesa restaurante : set){
                    restaurantesMap.put(restaurante.getId(), restaurante);
                }
                contextoApp.setAttribute("restaurantesMap", restaurantesMap);
            }
            if(restaurantesMap.isEmpty()){
                System.out.println("Error: No se ha encontrado ningun restaurante");
            }
            else{
                GestorMesa restaurante = restaurantesMap.get(id);
                if(restaurante!= null){
                    System.out.println("El restaurante " + restaurante.getId() + " tiene un horario = " + restaurante.getHorario());
                    res = gson.toJson(restaurante);
                    sesion.setAttribute("selectedRestaurante", restaurante);
                }
                
                //System.out.println(res);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return res;
    }
    
    public static GestorMesa findRestaurante(int id, HttpServletRequest request){
        //Mira si el restaurante esta cacheado
        HttpSession sesion = request.getSession();
        ServletContext contextoApp = sesion.getServletContext();
        
        HashMap<Integer, GestorMesa> restaurantesMap = (HashMap) contextoApp.getAttribute("restaurantesMap");
        if(restaurantesMap == null){
            try {
                restaurantesMap = new HashMap();
                RestauranteDAO dao = new RestauranteDAO();
                TreeSet<GestorMesa> set = (TreeSet) dao.findAllRestaurantes();
                for(GestorMesa restaurante : set){
                    restaurantesMap.put(restaurante.getId(), restaurante);
                }
                contextoApp.setAttribute("restaurantesMap", restaurantesMap);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(GestorRestaurante.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        GestorMesa res = restaurantesMap.get(id);
        return res;
    }
}
