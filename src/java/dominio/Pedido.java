/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Alvaro
 */
public class Pedido implements Comparable<Pedido>{
    
    private int id;
    private int idRestaurante;
    private int idMesa;
    private Usuario usuario;
    private String hora; // HH:mm
    private String comentarios;
    
    private ArrayList<Producto> productos;

    public Pedido(int id, int idRestaurante, int idMesa, Usuario usuario, String hora, String comentarios, ArrayList<Producto> productos) {
        this.id = id;
        this.idRestaurante = idRestaurante;
        this.idMesa = idMesa;
        this.usuario = usuario;
        this.hora = hora;
        this.comentarios = comentarios;
        this.productos = new ArrayList();
        this.productos.addAll(productos);
    }

    public Pedido(int id, int idRestaurante, int idMesa, Usuario usuario, String hora, String comentarios) {
        this.id = id;
        this.idRestaurante = idRestaurante;
        this.idMesa = idMesa;
        this.usuario = usuario;
        this.hora = hora;
        this.comentarios = comentarios;
        this.productos = new ArrayList();
    }
    
    //Para comparar pedidos 
    public Pedido(int id, int idRestaurante, int idMesa) {
        this.id = id;
        this.idRestaurante = idRestaurante;
        this.idMesa = idMesa;
    }
    
    public float getPrecioTotal(){
        float total = 0;
        for(Producto producto : productos){
            total+=producto.getPrecio();
        }
        return total;
    }
    
    
    
    public void addProducto(Producto producto){
        this.productos.add(producto);
    }
    
    public void addProductos(Collection<Producto> productos){
        this.productos.addAll(productos);
    }
    
    public void removeProducto(Producto producto){
        if(producto != null)
            this.productos.remove(producto);
    }
    
    public void clearProductos(){
        this.productos.clear();
    }
    
    public Producto findProducto(Producto producto){
        for(Producto result : this.productos){
            if(result.equals(producto)){
                return result; 
            }
        }
        return null;
    }
    
    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Pedido{" + "id=" + id + ", idRestaurante=" + idRestaurante + ", idMesa=" + idMesa + ", usuario=" + usuario + ", hora=" + hora + ", comentarios=" + comentarios + ", productos=" + productos + '}';
    }

    

    @Override
    public int compareTo(Pedido o) {
        //Ordenar por hora
        if(this.equals(o))
            return 0;
        else{
            int[] time = Horario.horaToInt(this.hora);
            int[] otherTime = Horario.horaToInt(this.hora);
            return (time[0] - otherTime[0])*60 + (time[1] - otherTime[1]);
        }
    }
}
