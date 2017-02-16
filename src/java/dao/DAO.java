/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author Alvaro Clemente
 */
public abstract class DAO {
    static Connection c  = null;
    
    public DAO() throws ClassNotFoundException, SQLException{
        
        if(c==null || c.isValid(200)){
            Class.forName("com.mysql.jdbc.Driver");
            String bd = "jdbc:mysql://localhost/tfg";
            c = DriverManager.getConnection(bd, "server", "tfgserver");
        }
    }
    
}
