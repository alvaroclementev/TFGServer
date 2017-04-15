/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

/**
 *
 * @author Alvaro
 */
public class Usuario {
    private String email;
    private String nombre;
    private String apellidos;

    public Usuario(String email, String nombre, String apellidos) {
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Usuario(String email) {
        this.email = email;
    }
      
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    
    
}
