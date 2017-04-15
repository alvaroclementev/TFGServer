/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import com.google.gson.Gson;
import dao.ProductoDAO;
import dominio.Producto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import servlets.ControllerProductos;

/**
 *
 * @author Alvaro
 */
public class GestorProducto {
    private Collection<Producto> carrito;
    private HttpServletRequest request;
    //private ServletContext contextoApp;
    private HttpServletResponse response;

    public GestorProducto(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response) {
      //  this.contextoApp= servlet.getServletContext();
      
      HttpSession sesion = request.getSession();
      this.carrito = (Collection) sesion.getAttribute("carrito");
      if(this.carrito == null){
          this.carrito = new ArrayList();
          sesion.setAttribute("carrito", carrito);
          
      }
      this.request = request;
      this.response = response;
    }
    
    public String buscarProducto(){
        
        String res = "No se han encontrado productos";
        String nombre = request.getParameter("nombre");
        Producto producto = null;
        ProductoDAO dao;
        try {
            dao = new ProductoDAO();
            producto = dao.findProductoByNombre(nombre);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(producto != null){
            Gson gson = new Gson();
            res = gson.toJson(producto);
        }
        return res;
    }
    
    
    public String buscarProductoNoJSON(){
        
        String vista = "error.html";
        String nombre = request.getParameter("nombre");
        Producto producto = null;
        ProductoDAO dao;
        try {
            dao = new ProductoDAO();
            producto = dao.findProductoByNombre(nombre);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(producto != null)
            request.setAttribute("producto", producto);
            vista = "resultado.jsp";      
        
        return vista;
    }
    
    public String mostrarProductosNoJSON(){
        
        String vista = "error.html";
        try {
            
            ProductoDAO dao = new ProductoDAO();
            TreeSet<Producto> set = (TreeSet) dao.findAllProductos();
            if(set.isEmpty()){
                System.out.println("Error: No se ha encontrado ningun producto");
            }
            else{
                request.setAttribute("set", set);
                vista = "mostrar.jsp";
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vista;
    }
    
    public String mostrarProductos(){
        
        Gson gson = new Gson();
        String res = "No se han encontrado productos";
        try {            
            ProductoDAO dao = new ProductoDAO();
            TreeSet<Producto> set = (TreeSet) dao.findAllProductos();
            if(set.isEmpty()){
                System.out.println("Error: No se ha encontrado ningun producto");
            }
            else{
                res = gson.toJson(set);
                //System.out.println(res);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return res;
    } 
    
    
    
    
}
