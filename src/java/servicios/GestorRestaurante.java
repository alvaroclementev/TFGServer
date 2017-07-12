/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import com.google.gson.Gson;
import dao.RestauranteDAO;
import dominio.Restaurante;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alvaro
 */
public abstract class GestorRestaurante {
   
    public static String mostrarRestaurantes(ServletContext contextoApp){
        Gson gson = new Gson();
        String res = "No se han encontrado restaurantes";
        //Lista de Restaurantes deberia estar cacheada
        HashMap<Integer, Restaurante> restaurantesMap = (HashMap) contextoApp.getAttribute("restaurantesMap");
        if(restaurantesMap == null){
            restaurantesMap = initRestauranteMap(contextoApp);
        }
        Collection<Restaurante> values = restaurantesMap.values();
        if(values.isEmpty()){
            System.out.println("Error: No se ha encontrado ningun restaurante");
        }
        else{
            res = gson.toJson(values);
            //System.out.println(res);
        }
        
        return res;
    }
    
    public static String seleccionarRestaurante(int id, HttpServletRequest request){
        Gson gson = new Gson();
        String res = "No se han encontrado el restaurante";
        HttpSession sesion = request.getSession();
        ServletContext contextoApp = sesion.getServletContext();
        //Lista de Restaurantes deberia estar cacheada
        HashMap<Integer, Restaurante> restaurantesMap = (HashMap) contextoApp.getAttribute("restaurantesMap");
        if(restaurantesMap == null){
            restaurantesMap = initRestauranteMap(contextoApp);
        }
        if(restaurantesMap.isEmpty()){
            System.out.println("Error: No se ha encontrado ningun restaurante");
        }
        else{
            Restaurante restaurante = restaurantesMap.get(id);
            if(restaurante!= null){
                System.out.println("El restaurante " + restaurante.getId() + " tiene un horario = " + restaurante.getGestorMesa().getHorario());
                res = gson.toJson(restaurante);
                sesion.setAttribute("selectedRestaurante", restaurante);
            }
            
            //System.out.println(res);
        }
        
        return res;
    }
    
    public static Restaurante findRestaurante(int id, HttpServletRequest request){
        //Mira si el restaurante esta cacheado
        ServletContext contextoApp = request.getServletContext();
        
        HashMap<Integer, Restaurante> restaurantesMap = (HashMap) contextoApp.getAttribute("restaurantesMap");
        //Lista de Restaurantes deberia estar cacheada
        if(restaurantesMap == null){
            restaurantesMap = initRestauranteMap(contextoApp);
        }
        Restaurante res = restaurantesMap.get(id);
        return res;
    }
    
    public static Collection<Restaurante> findAllRestaurantes(ServletContext contextoApp){
        HashMap<Integer, Restaurante> restaurantesMap = (HashMap) contextoApp.getAttribute("restaurantesMap");
        //Lista de Restaurantes deberia estar cacheada
        if(restaurantesMap == null){
            restaurantesMap = initRestauranteMap(contextoApp);
        }
        return restaurantesMap.values();
    }
    
    private static HashMap<Integer, Restaurante> initRestauranteMap(ServletContext contextoApp){
        
        HashMap<Integer, Restaurante> restaurantesMap = new HashMap();
        try {
            RestauranteDAO dao = new RestauranteDAO();
            TreeSet<Restaurante> set = (TreeSet) dao.findAllRestaurantes();
            for(Restaurante restaurante : set){
                restaurantesMap.put(restaurante.getId(), restaurante);
            }
            contextoApp.setAttribute("restaurantesMap", restaurantesMap);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(GestorRestaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return restaurantesMap;
    }
   
}
