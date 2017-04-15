/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author Alvaro
 */
import dominio.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioDAO extends DAO{
    
    private static final String QUERY_FIND_USER_BY_EMAIL = "SELECT * FROM usuarios WHERE email= ? ";
    private static final String QUERY_INSERT_USER = "INSERT INTO usuarios (email, nombre, apellidos) VALUES (?, ?, ?)";
    
    public UsuarioDAO() throws ClassNotFoundException, SQLException{
        super();
    }
    
    public Usuario findUsuarioByEmail(String email) throws SQLException{
        PreparedStatement ps = c.prepareStatement(QUERY_FIND_USER_BY_EMAIL);
        ps.setString(1, email);
        
        ResultSet rs = ps.executeQuery();
        Usuario usuario = new Usuario("Vacio");
        while(rs.next()){
            String nombre = rs.getString("nombre");
            String apellidos = rs.getString("apellidos");
                        
            usuario = new Usuario(email, nombre, apellidos);
        }
        
        rs.close();
        ps.close();
        
        return usuario;
    }
    
    public boolean existsUsuario(String email) throws SQLException{
        PreparedStatement ps = c.prepareStatement(QUERY_FIND_USER_BY_EMAIL);
        ps.setString(1, email);
        
        ResultSet rs = ps.executeQuery();
        boolean result = false;
        while(rs.next()){
            //email es primary key, no puede haber mas de 1 cuenta con mismo email
            result = true; 
        } 
        rs.close();
        ps.close();
               
        return result;
    }
    
    public void addUsuario(Usuario usuario) throws SQLException{
        PreparedStatement ps = c.prepareStatement(QUERY_INSERT_USER);
        ps.setString(1, usuario.getEmail());
        ps.setString(2, usuario.getNombre());
        ps.setString(3, usuario.getApellidos());
        
        ps.executeUpdate();
        ps.close();               
    }
}
