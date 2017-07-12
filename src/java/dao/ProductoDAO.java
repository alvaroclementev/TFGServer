/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dominio.Producto;
import dominio.exceptions.PrecioInvalidoException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alvaro Clemente
 */
public class ProductoDAO extends DAO{
    
    private static final String QUERY_FIND_PROD_BY_ID = "SELECT * FROM productos WHERE id= ? ";
    private static final String QUERY_FIND_PROD_BY_NOMBRE = "SELECT * FROM productos WHERE nombre= ? ";
    private static final String QUERY_FIND_ALL_PROD_BY_ID = "SELECT * FROM productos WHERE restauranteId= ? ";
    
    public ProductoDAO() throws ClassNotFoundException, SQLException{
        super();
    }
    
    public Producto findProductoById(int id) throws SQLException{
        PreparedStatement ps = c.prepareStatement(QUERY_FIND_PROD_BY_ID);
        ps.setInt(1, id);
        
        ResultSet rs = ps.executeQuery();
        Producto producto = null;
        while(rs.next()){
            String nombre = rs.getString("nombre");
            String descripcion = rs.getString("descripcion");
            float precio = rs.getFloat("precio");
            
            producto = new Producto(id, nombre, descripcion, precio);
        }
        
        rs.close();
        ps.close();
        
        return producto;
    }
    
    public Producto findProductoByNombre(String nombre) throws SQLException{
        PreparedStatement ps = c.prepareStatement(QUERY_FIND_PROD_BY_NOMBRE);
        ps.setString(1, nombre);
        
        ResultSet rs = ps.executeQuery();
        Producto producto = null;
        while(rs.next()){
            int id = rs.getInt("id");
            String descripcion = rs.getString("descripcion");
            float precio = rs.getFloat("precio");
            
            producto = new Producto(id, nombre, descripcion, precio);
        }
        
        if(producto == null){
            producto = new Producto(0, "Producto no encontrado", "Producto no encontrado", 0);
        }
        rs.close();
        ps.close();
        
        return producto;
    }
    
    public Set<Producto> findAllProductos(int restauranteId) throws SQLException{
        PreparedStatement ps = c.prepareStatement(QUERY_FIND_ALL_PROD_BY_ID);
        ps.setInt(1, restauranteId);
        TreeSet<Producto> set = new TreeSet();
        
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String descripcion = rs.getString("descripcion");
            float precio = rs.getFloat("precio");
            
            Producto producto = new Producto(id, nombre, descripcion, precio);
            set.add(producto);
        }
        
        rs.close();
        ps.close();
        
        return set;
    }
}
