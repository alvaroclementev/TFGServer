/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import servicios.GestorPedido;
import servlets.ControllerPedidos;

/**
 *
 * @author Alvaro
 */
public class Reserva implements Comparable<Reserva>{
    
    private int restauranteId;
    private int reservaId;
    private TreeSet<Usuario> comensales;
    private Mesa mesa;
    private long hora;
    private String restauranteName;
    private String horaString;
    
    @Expose(serialize = false)
    private GestorPedido gestorPedido = new GestorPedido();

    public Reserva(int restauranteId, int reservaId, TreeSet<Usuario> comensales, Mesa mesa, long hora, String restauranteName, String horaString) {
        this.restauranteId = restauranteId;
        this.reservaId = reservaId;
        this.comensales = comensales;
        this.mesa = mesa;
        this.hora = hora;
        this.restauranteName = restauranteName;
        this.horaString = horaString;
    }
    
    public Reserva(int restauranteId, int reservaId, Mesa mesa, long hora, String restauranteName, String horaString) {
        this.restauranteId = restauranteId;
        this.reservaId = reservaId;
        this.mesa = mesa;
        this.hora = hora;
        this.restauranteName = restauranteName;
        this.horaString = horaString;
        this.comensales = new TreeSet();
    }

    public Reserva(int restauranteId, int reservaId) {
        this.restauranteId = restauranteId;
        this.reservaId = reservaId;
    }
    
    //Funcionalidades
    
    public String hacerPedido(HttpServletRequest request, Restaurante selectedRestaurante){
        String message;
        
        //Ver usuario que hace la reserva
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if(usuario == null){
            return "Error, el usuario no ha iniciado sesion";
        }
        if(!comensales.contains(usuario)){
            return "Error, el usuario no forma parte de esta reserva!";
        }
        
        String pedidoRequest = request.getParameter("pedido");
        if(pedidoRequest == null){
            return "Error: no se ha encontrado el pedido en la request";
            
        }
        System.out.println("El JSON del pedido recibido es: " + pedidoRequest);
        Gson gson = new Gson();
        try{
            //El pedido que llega es una collection de Productos 
            ArrayList<Producto> productos = gson.fromJson(pedidoRequest, ArrayList.class);
            if(productos == null || productos.isEmpty()){
                return "Error: no se ha recibido ningun producto!";
            }
            //TODO CORRECTO, SE AÑADE EL PEDIDO
            //Coger los comentarios del pedido
            String comentarios = request.getParameter("comentarios");
                        
            String horaPedido = request.getParameter("hora");
            if(horaPedido == null)
                horaPedido = "-1"; //Lo antes posible
            Pedido pedido = new Pedido(selectedRestaurante.getNextPedidoId(), selectedRestaurante.getId(), getMesa().getId(), usuario, horaPedido, comentarios);
            System.out.println("Se ha confirmado el pedido " + pedido);
            pedido.addProductos(productos);
            gestorPedido.addToPendientes(pedido);
            selectedRestaurante.addToHistorial(pedido);
            message = gson.toJson(pedido);
            
        } catch(JsonSyntaxException ex){
            message = "Error: No se ha podido parsear el pedido";
            Logger.getLogger(ControllerPedidos.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return message;
    }
    
    public String checkout(HttpServletRequest request, Restaurante selectedRestaurante){
        String message;
        //Necesitas la reserva y con eso tienes todo
        //Ver usuario que hace la reserva
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if(usuario == null){
            return "Error, el usuario no ha iniciado sesion";
        }
        if(!comensales.contains(usuario)){
            return "Error, el usuario no forma parte de esta reserva!";
        }
        
        //Comprobar que no hay pagos pendientes
        //FIXME: AÑADIR FUNCIONALIDAD DE PAGAR, AHORA MISMO PAGA TODO AUTOMATICAMENTE
        gestorPedido.pagarPendientes(gestorPedido.getPendientes());
        
        //SE HACE EL CHECKOUT
        if(!gestorPedido.getPendientes().isEmpty()){
            return "Error: aun hay pagos pendientes";
        }
        //EXITO
        return "Checkout terminado con éxito";
    }
    
    public String pedidoFuturo(HttpServletRequest request, Restaurante selectedRestaurante){
        String message;
        
        //Ver usuario que hace la reserva
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if(usuario == null){
            return "Error, el usuario no ha iniciado sesion";
        }
        if(!comensales.contains(usuario)){
            return "Error, el usuario no forma parte de esta reserva!";
        }
        
        String pedidoRequest = request.getParameter("pedido");
        if(pedidoRequest == null){
            return "Error: no se ha encontrado el pedido en la request";
            
        }
        System.out.println("El JSON del pedido recibido es: " + pedidoRequest);
        Gson gson = new Gson();
        try{
            //El pedido que llega es una collection de Productos 
            ArrayList<Producto> productos = gson.fromJson(pedidoRequest, ArrayList.class);
            if(productos == null || productos.isEmpty()){
                return "Error: no se ha recibido ningun producto!";
            }
            //Coger los comentarios del pedido
            String comentarios = request.getParameter("comentarios");
                        
            String horaPedido = request.getParameter("hora");
            if(horaPedido == null){
                return "Error: no hay una hora especificada";
            }  
            //TODO CORRECTO, SE AÑADE EL PEDIDO         
            Pedido pedido = new Pedido(selectedRestaurante.getNextPedidoId(), selectedRestaurante.getId(), getMesa().getId(), usuario, horaPedido, comentarios);
            pedido.addProductos(productos);
            gestorPedido.addToPendientes(pedido);
            selectedRestaurante.addToHistorial(pedido);
            message = "Pedido registrado correctamente";
            
        } catch(JsonSyntaxException ex){
            message = "Error: No se ha podido parsear el pedido";
            Logger.getLogger(ControllerPedidos.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return message;
    }
    
    //Metodos para pedidos
    public void addPedido(Pedido pedido){
        gestorPedido.addToPendientes(pedido);
    }
    
    public Collection<Pedido> getPedidosAntemano(){
        return gestorPedido.getPendientes();
    }
    
    //Getters & Setters
    public int getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(int restauranteId) {
        this.restauranteId = restauranteId;
    }   
        
    public int getReservaId() {
        return reservaId;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    
    public long getHora() {
        return hora;
    }

    public void setHora(long hora) {
        this.hora = hora;
    }

    public String getRestauranteName() {
        return restauranteName;
    }

    public void setRestauranteName(String restauranteName) {
        this.restauranteName = restauranteName;
    }

    public String getHoraString() {
        return horaString;
    }

    public void setHoraString(String horaString) {
        this.horaString = horaString;
    }

    public TreeSet<Usuario> getComensales() {
        return comensales;
    }

    public void setComensales(TreeSet<Usuario> comensales) {
        this.comensales = comensales;
    }

    public void addComensal(Usuario comensal){
        this.comensales.add(comensal);
    }
    
    public void removeComensal(Usuario comensal){
        this.comensales.remove(comensal);
    }
    
    public boolean isComensal(Usuario comensal){
        return this.comensales.contains(comensal);
    }

    @Override
    public String toString() {
        return "Reserva{" + "restauranteId=" + restauranteId + ", reservaId=" + reservaId + ", comensales=" + comensales + ", mesa=" + mesa + ", hora=" + hora + ", restauranteName=" + restauranteName + ", horaString=" + horaString + ", gestorPedido=" + gestorPedido + '}';
    }   
        
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.restauranteId;
        hash = 37 * hash + this.reservaId;
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
        final Reserva other = (Reserva) obj;
        if (this.reservaId != other.reservaId) {
            return false;
        }
        if (this.restauranteId != other.restauranteId) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Reserva o) {
        if(this.hora == o.getHora())
            return this.hashCode() - o.hashCode();
        else
            return new Long(this.hora).compareTo(new Long(o.getHora()));
    }

}
