/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import dominio.exceptions.PrecioInvalidoException;

/**
 *
 * @author Alvaro Clemente
 */
public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private float precio;
    //TODO Añadir un vector de opciones para modificar
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) throws PrecioInvalidoException{
        if(precio >= 0){
            this.precio = precio;
        }
        else{
            throw new PrecioInvalidoException(precio);
        }
        
    }

    public Producto(int id, String nombre, String descripcion, float precio){
        
        try{
            this.setPrecio(precio);
            this.id = id;
            this.nombre = nombre;
            this.descripcion = descripcion;
        }
        catch(PrecioInvalidoException e){
            e.printStackTrace();
        }
    }
    
}
