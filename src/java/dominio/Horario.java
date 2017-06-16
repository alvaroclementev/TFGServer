/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Alvaro
 */
public class Horario {
    //FIXME: que pasa si en vez de fecha absoluta cogemos solo hora del dia?
    private static final int TIEMPO_RESERVA = 180; //Minutos
    
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private int granularidad; //Tamaño bloque en minutos
    private int numeroBloques;
    private int bloquesPorReserva;
    
    private ArrayList<Integer> bloques; //booleano indicando si ese bloque esta ocupado y si lo esta qué reserva lo ha hecho
                                        //0 indica mesa libre

    public Horario(LocalDateTime horaInicio, LocalDateTime horaFin, int granularidad) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.granularidad = granularidad;
        initBloques();
        
        this.bloques = new ArrayList();
        for(int i=0; i<numeroBloques; i++){
            this.bloques.add(0); //Libre por defecto
        }
        
    }
    
    public Horario(Horario other) {
        this.horaInicio = other.getHoraInicio();
        this.horaFin = other.getHoraFin();
        this.granularidad = other.getGranularidad();
        initBloques();
        
        this.bloques = new ArrayList();
        for(int i=0; i<numeroBloques; i++){
            this.bloques.add(0); //Libre por defecto
        }
        
    }
     
    private void initBloques(){
        long minutes = ChronoUnit.MINUTES.between(horaInicio, horaFin);
        this.numeroBloques = (int) Math.ceil(minutes/granularidad);
        
        bloquesPorReserva = (int) Math.ceil(TIEMPO_RESERVA/granularidad);
    }
    
    
    public boolean isFreeAt(LocalDateTime hora){
        //Antes del inicio del turno
        if(hora.isBefore(horaInicio)){
            System.out.println("La hora es anterior al inicio del turno");
            System.out.println("horaInicio= " + horaInicio.toString() + " hora = " + hora.toString());
            return false;
        }
        else if(hora.isAfter(horaFin)){
            System.out.println("La hora es posterior al fin del turno");
            return false;
        }
        int index = getIndexFor(hora);
        if(index < 0){
            System.out.println("Ha pasado algo raro, getIndexFor()<0");
            return false;
        }
        return isBlockAvailable(index);
        
    }
    
    private boolean isBlockAvailable(int bloque){
        boolean result = false;
        //Comprobar que no hay una reserva en un periodo razonable (3 horas)
        for(int i=bloque; i< (bloque + bloquesPorReserva); i++){
            if(i >= bloques.size()){
                //Has llegado al final
                break;
            }
            if(bloques.get(i) != 0){
                return false; //Mesa ocupada
            }            
            result = true;
        }
             
        return result;
    }
    
    public void reservarAt(LocalDateTime hora, int reservaId){
        //No se hacen comprobaciones de la fecha porque se llama primero a isFreeAt() que ya las ha hecho
        int bloque = this.getIndexFor(hora);
        for(int i = bloque; i<(bloque + bloquesPorReserva); i++){
            if(i >= bloques.size()){
                //Has llegado al final
                break;
            }
            bloques.set(i, reservaId);
        }
    }
    
    private int getIndexFor(LocalDateTime hora){
        /** Devuelve el inidice del primer bloque correspondiente a la hora que 
         * se quiere reservar **/
        
        //Se comprueba que no esta fuera de limites fuera del metodo
        long minutes = ChronoUnit.MINUTES.between(horaInicio, hora);
        int index = (int) Math.ceil(minutes/granularidad);
        
        return index;
    }
    
    public void eliminarReserva(int idReserva){
        for(int i=0; i<bloques.size(); i++){
            if(bloques.get(i) == idReserva){
                bloques.set(i, 0);
            }
        }
    }
    
    
    public List<String> getHorasLibres(){
        /**
         * Devuelve un string con las horas libres desde el momento que se pregunta
         */
        LinkedList<String> result = new LinkedList();
        
        //Asegurarte que estas en la franja correcta
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(horaInicio)){
            System.out.println("La hora es anterior al inicio del turno");
            System.out.println("horaInicio= " + horaInicio.toString() + " hora = " + now.toString());
            return result;
        }
        else if(now.isAfter(horaFin)){
            System.out.println("La hora es posterior al fin del turno");
            return result;
        }
        int nowIndex = getIndexFor(now);
        int horaDeInicio = this.horaInicio.getHour();
        int minutoDeInicio = this.horaInicio.getMinute();
        int minutoDeBloque = minutoDeInicio + nowIndex*this.granularidad;
        for(int i = nowIndex; i< numeroBloques; i++){
            if(isBlockAvailable(i)){
                //Este bloque esta disponible, lo traducimos a hora en String y lo añadimos
                //FIXME: Esto por ahora solo devuelve la hora local del servidor
                int horaDeBloque = horaDeInicio + minutoDeBloque/60;
                int minutoAdd = minutoDeBloque % 60;
                String horaParsed = Integer.toString(horaDeBloque);
                if(horaDeBloque < 10){
                    horaParsed = "0" + horaParsed;
                }
                String minutoParsed = Integer.toString(minutoAdd);
                if(minutoAdd < 10){
                    minutoParsed = "0" + minutoParsed;
                }
                String toAdd = horaParsed + ":" + minutoParsed;
                result.add(toAdd);
            }   
            minutoDeBloque += granularidad;
        }
        return result;
    }
    
    public int getIdReservaAt(LocalDateTime hora){
        int index = this.getIndexFor(hora);
        return bloques.get(index);
    }
    
    //Getters & Setters
    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalDateTime horaFin) {
        this.horaFin = horaFin;
    }

    public int getGranularidad() {
        return granularidad;
    }

    public void setGranularidad(int granularidad) {
        this.granularidad = granularidad;
        //Si cambia la granularidad, cambian las constantes de los bloques
        initBloques();
    }

    public ArrayList getBloques() {
        return bloques;
    }

    public void setBloques(ArrayList bloques) {
        this.bloques = bloques;
    }
}
