/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import dominio.exceptions.PrecioInvalidoException;
import java.util.Objects;

/**
 *
 * @author Alvaro Clemente
 */
public class Producto implements Comparable{
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

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Producto other = (Producto) obj;
        return this.id == other.getId();
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Producto)){
            throw new ClassCastException();
        }
        else{
            Producto p = (Producto) o;
            return this.id - p.getId();              
        }
    }
    
}
