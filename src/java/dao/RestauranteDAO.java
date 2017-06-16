/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.DAO.c;
import dominio.Mesa;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import servicios.GestorMesa;

/**
 *
 * @author Alvaro
 */
public class RestauranteDAO extends DAO{
    
    private static final String QUERY_FIND_ALL_RESTAURANTES = "SELECT * FROM restaurantes";
    private static final String QUERY_FIND_RESTAURANTE_BY_ID = "SELECT * FROM restaurantes WHERE id= ? ";
    private static final String QUERY_FIND_MESAS_BY_RESTAURANTE_ID = "SELECT id, maxComensales FROM mesas WHERE restaurante = ? ";
        
    
    public RestauranteDAO() throws ClassNotFoundException, SQLException{
        super();
    }
    
    public Collection<GestorMesa> findAllRestaurantes() throws SQLException{
        TreeSet<GestorMesa> set;
        try (PreparedStatement ps = c.prepareStatement(QUERY_FIND_ALL_RESTAURANTES)) {
            ResultSet rs = ps.executeQuery();
            set = new TreeSet();
            while(rs.next()){
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("telefono");
                String descripcion = rs.getString("descripcion");
                String categoria = rs.getString("categoria");
                
                ArrayList<Mesa> mesas = (ArrayList) findAllMesasFromRestaurante(id);
                set.add(new GestorMesa(id, nombre, direccion, telefono, descripcion, categoria, mesas));
            }
        }
        
        return set;
    }
    
    public GestorMesa findRestauranteById(int id) throws SQLException{
        ResultSet rs;
        String nombre;
        String direccion;
        String telefono;
        String descripcion;
        String categoria;
        try (PreparedStatement ps = c.prepareStatement(QUERY_FIND_RESTAURANTE_BY_ID)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            nombre = null;
            direccion = null;
            telefono = null;
            descripcion = null;
            categoria = null;
            while(rs.next()){
                nombre = rs.getString("nombre");
                direccion = rs.getString("direccion");
                telefono = rs.getString("telefono");
                descripcion = rs.getString("descripcion");
                categoria = rs.getString("categoria");
                
            }   rs.close();
        }
        
        ArrayList<Mesa> mesas = (ArrayList<Mesa>) findAllMesasFromRestaurante(id);
        
        GestorMesa restaurante = new GestorMesa(id, nombre, direccion, telefono, descripcion, categoria, mesas);
             
        return restaurante;
    }
    
    public GestorMesa findRestauranteByName(String nombre) throws SQLException{
        int id;
        try (PreparedStatement ps = c.prepareStatement(QUERY_FIND_RESTAURANTE_BY_ID)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            id = -1;
            while(rs.next()){            
                id = rs.getInt("id");
            }
        }
        if(id != -1)
            return findRestauranteById(id);
        else{
            return null;
        }
    }
    
    public Collection<Mesa> findAllMesasFromRestaurante(int id) throws SQLException {
        ArrayList<Mesa> mesas;
        try (PreparedStatement ps2 = c.prepareStatement(QUERY_FIND_MESAS_BY_RESTAURANTE_ID)) {
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();
            mesas = new ArrayList();
            while(rs2.next()){
                int mesaId = rs2.getInt("id");
                int maxComensales = rs2.getInt("maxComensales");
                Mesa mesa = new Mesa(mesaId, maxComensales);
                mesas.add(mesa);
            }   rs2.close();
        }
        return mesas;
    }
    
}
