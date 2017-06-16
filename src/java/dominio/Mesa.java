/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Alvaro
 */
public class Mesa implements Comparable<Mesa>{
    
    private int id; 
    private HashSet<Usuario> comensales;
    private boolean ocupada;
    private int maxComensales;
    private Horario horario;

    public Mesa(int id, int maxComensales, Horario horario) {
        this.id = id;
        this.maxComensales = maxComensales;
        this.horario = horario;
        this.ocupada = false;
        
        comensales = new HashSet();
    }

    public Mesa(int id, int maxComensales) {
        this.id = id;
        this.maxComensales = maxComensales;
       
        comensales = new HashSet();
    }
      
    public boolean reservarAt(LocalDateTime fecha, Usuario usuario, int reservaId){
        if(!isDisponibleAt(fecha)){
            return false;
        }
        if(comensales.size() >= maxComensales){
            return false;
            //Dar error de mesa llena?
        }
        //Esta disponible la mesa para reservar, ahora se reserva:
        horario.reservarAt(fecha, reservaId);
        setOcupada(true);
        
        //Añadir usuario a comensales
        comensales.add(usuario);
        return true;
    }
    
    public boolean sentar(Usuario usuario, int reservaId){
       LocalDateTime fecha = LocalDateTime.now();
       if(!isDisponibleAt(fecha)){
            return false;
        }
        //Esta disponible la mesa para reservar, ahora se reserva:
        horario.reservarAt(fecha, reservaId);
        setOcupada(true);
        
        //Añadir usuario a comensales
        comensales.add(usuario);
        
        return true;
    }
    
    public void levantar(Usuario usuario, int idReserva){
        horario.eliminarReserva(idReserva);
        comensales.remove(usuario);
        if(comensales.isEmpty()){
            setOcupada(false);
        }
    }
    
    public int getIdReservaAt(LocalDateTime hora){
        return horario.getIdReservaAt(hora);
    }
    
    public void cancelarReserva(int idReserva){
        horario.eliminarReserva(idReserva);
    }
    
    public List<String> getHorasLibres(){
        return this.horario.getHorasLibres();
    }
    
    public boolean isDisponibleNow(){
        if(isOcupada()){
            return false;
        }
        else{
            //Comprobar reservas futuras
            return isDisponibleAt(LocalDateTime.now());
        }
    }
    
    public boolean isDisponibleAt(LocalDateTime fecha) {
        return horario.isFreeAt(fecha);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    private boolean isOcupada() {
        return ocupada;
    }

    private void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public HashSet<Usuario> getComensales() {
        return comensales;
    }

    public void setComensales(HashSet<Usuario> comensales) {
        this.comensales = comensales;
    }
    
    public int addComensal(Usuario comensal){
        //TODO: Gestionar errores con exceptions
        int returnCode;
        if(comensales.contains(comensal)){
            returnCode = -1; //Usuario ya sentado
        }
        else{
            if(comensales.size() >= maxComensales){
                returnCode = -2; //Mesa llena
            }
            else{
                //Todo correcto, se añade el comensal
                comensales.add(comensal);
                returnCode = 1;
            }
        }
        return returnCode;
    }

   
    
    
    
    public void vaciarMesa(){
        comensales.clear();
        setOcupada(false);
    }   

    public int getMaxComensales() {
        return maxComensales;
    }

    public void setMaxComensales(int maxComensales) {
        this.maxComensales = maxComensales;
    }

    
    
    @Override
    public String toString() {
        return "Mesa{" + "id=" + id + ", maxComensales=" + maxComensales + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.id;
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
        final Mesa other = (Mesa) obj;
        return this.id == other.id;
    }
   
    @Override
    public int compareTo(Mesa o) {
        return this.id - o.getId();
    } 
        
    
}
