/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio.exceptions;

/**
 *
 * @author Alvaro
 */
public class UserAlreadyExistsException extends Exception{
    
    private String email;
    
    public UserAlreadyExistsException(String email){
        this.email = email;
    }
    
    @Override
    public String toString(){
        return "Ya existe un usuario con email: " + email;
    }
}
