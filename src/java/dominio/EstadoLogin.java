/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Esta clase representa el estado que necesita un usuario al hacer login
 * en un dispositivo nuevo para continuar funcionando normalmente
 * 
 * @author Alvaro
 */
public class EstadoLogin {
    private Reserva reservaActiva;
    private Collection<Reserva> reservas = new TreeSet<>();
    
    //Estos objetos son la referencia al restaurante y a la mesa relacionadas a
    //la reserva activa, si existe;
    private Restaurante restauranteActivo;

    public EstadoLogin(Reserva reservaActiva, Collection<Reserva> reservas, Restaurante restauranteActivo) {
        this.reservaActiva = reservaActiva;
        this.reservas.addAll(reservas);
        this.restauranteActivo = restauranteActivo;
    }

    public EstadoLogin(Collection<Reserva> reservas) {
        this.reservas.addAll(reservas);
    }
    
    //Getters & Setters
    public Reserva getReservaActiva() {
        return reservaActiva;
    }

    public void setReservaActiva(Reserva reservaActiva) {
        this.reservaActiva = reservaActiva;
    }

    public Collection<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(Collection<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Restaurante getRestauranteActivo() {
        return restauranteActivo;
    }

    public void setRestauranteActivo(Restaurante restauranteActivo) {
        this.restauranteActivo = restauranteActivo;
    }

    @Override
    public String toString() {
        return "EstadoLogin{" + "reservaActiva=" + reservaActiva + ", reservas=" + reservas + ", restauranteActivo=" + restauranteActivo + '}';
    }    
}
