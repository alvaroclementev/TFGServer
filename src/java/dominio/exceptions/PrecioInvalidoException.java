/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio.exceptions;

/**
 *
 * @author Alvaro Clemente
 */
public class PrecioInvalidoException extends Exception{
    private float precio;
    
    public PrecioInvalidoException(float precio){
        this.precio = precio;
    }
    
    public String toString(){
        return "El precio" + precio +  "es invalido";
    }
    
}
