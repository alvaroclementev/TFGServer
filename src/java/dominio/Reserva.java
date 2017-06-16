/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.TreeSet;

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
    //private Pedido pedido;

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
        return "Reserva{" + "restauranteId=" + restauranteId + ", reservaId=" + reservaId + ", comensales=" + comensales + ", mesa=" + mesa + ", hora=" + hora + ", restauranteName=" + restauranteName + ", horaString=" + horaString + '}';
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
