/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import com.google.gson.Gson;
import dao.RestauranteDAO;
import dominio.Horario;
import dominio.Mesa;
import dominio.Reserva;
import dominio.Usuario;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alvaro
 */
public class GestorMesa implements Comparable<GestorMesa>{
    /** Clase que representa un restaurante 
     * 
     */
    private final int id;
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private final String descripcion;
    private final String categoria;
    
    private final ArrayList<Mesa> mesas;
    private Horario horario;
    
    private final ArrayList<Reserva> reservas;
    private final ArrayList<Reserva> reservasActivas;
    private int nextReservaId = 1;
    
    //No Singleton
    
    public GestorMesa(int id, String nombre, String direccion, String telefono, String descripcion, String categoria, ArrayList<Mesa> mesas, ArrayList<Reserva> reservas, ArrayList<Reserva> reservasActivas, Horario horario) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.mesas = mesas;
        this.reservas = reservas;
        this.reservasActivas = reservasActivas;
        this.horario = horario;
    }

    public GestorMesa(int id, String nombre, String direccion, String telefono, String descripcion, String categoria, ArrayList<Mesa> mesas) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.mesas = mesas;
        this.reservas = new ArrayList();
        this.reservasActivas = new ArrayList();
    }

    public GestorMesa(int id, String nombre, String direccion, String telefono, String descripcion, String categoria, ArrayList<Mesa> mesas, Horario horario) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.mesas = mesas;
        this.horario = horario;
        this.reservas = new ArrayList();
        this.reservasActivas = new ArrayList();
    }
    

    public GestorMesa(int id, String nombre, String direccion, String telefono, String descripcion, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.mesas = new ArrayList();
        this.reservas = new ArrayList();
        this.reservasActivas = new ArrayList();
    }
    
    //FIXME: MIGRAR A LOCALTIME (SI NO NECESITAMOS FECHA... SOLO RESERVAS DEL MISMO DIA?)
    //FIXME: ASOCIAR LA MESA AL RESTAURANTE SELECCIONADO
    public String reservarMesa(HttpServletRequest request){
        //Parametros: mesaId(int); hora(String);
        //Atributos: usuario(Usuario);
        
        String message;
        try{
            //Encuentra la mesa a reservar 
            int mesaID = Integer.parseInt(request.getParameter("mesaId"));
            Mesa mesa = findMesa(mesaID);
            if(mesa == null){
                System.out.println("La mesa con id " + mesaID + " no existe");
                message = "No se ha encontrado una mesa con ese id";
                return message;
            }
            
            //Ver usuario que hace la reserva
            HttpSession sesion = request.getSession();
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");
            if(usuario == null){
                return "Error, el usuario no ha iniciado sesion";
            }
            
            
            
            //Ver hora de la reserva
            String horaString = request.getParameter("horaReserva");
            if(horaString == null){
                return "Error, hora no encontrada";
            }
            System.out.println("La hora parseada es: " + horaString);
            String[] horaSplit = horaString.split(":");
            int horaInt = Integer.parseInt(horaSplit[0]);
            int minutosInt = Integer.parseInt(horaSplit[1]);
            LocalDate date = LocalDate.now();
            LocalTime horaParsed = LocalTime.of(horaInt, minutosInt);
                        
            //Cuidado con la zona horaria, esto coge la zona del sistema, que puede ir cambiando
            LocalDateTime hora = LocalDateTime.of(date, horaParsed);
                        
            //Si todo ha ido bien, reservar la mesa:
            if(mesa.isDisponibleAt(hora)){
                if(!mesa.reservarAt(hora, usuario, nextReservaId)){
                    System.out.println("Error en la reserva: reservaId = -1");
                    return "Error en la reserva";                
                } else{
                    
                    //TODO: AÑADIR PERSISTENCIA DE RESERVAS FUTURAS! (EN BASE DE DATOS)
                    //RESERVA CORRECTA
                    Long millis = ZonedDateTime.of(hora, ZoneId.systemDefault()).toInstant().toEpochMilli();
                    Reserva reserva = new Reserva(this.getId(), nextReservaId, mesa, millis, this.getNombre(), horaString);
                    reserva.addComensal(usuario);
                    nextReservaId++;
                    Gson gson = new Gson();
                    message = gson.toJson(reserva);
                    
                    reservas.add(reserva);
                }
            }
            else{
                System.out.println("Error en reserva: la mesa está ocupada");
                return "Error en reserva: la mesa está ocupada";
            }
            
            
            
            
        } catch(NumberFormatException nfe){
            System.out.println("El id no es un int parseable\n"+ nfe);
            message = "Error: El id no es un int parseable\n"+nfe ;
        }
        
        return message;        
    }
    
    public String sentarEnMesa(HttpServletRequest request){
        String message;
        //int result = gestor.reservarMesa(mesa, usuario, LocalDateTime.MAX);
        try{
            //Encuentr la mesa a reservar 
            int mesaID = Integer.parseInt(request.getParameter("mesaId"));
            Mesa mesa = findMesa(mesaID);
            if(mesa == null){
                System.out.println("La mesa con id " + mesaID + " no existe");
                message = "Error: No se ha encontrado una mesa con ese id";
                return message;
            }
            
            //Esta ocupada?
            if(!mesa.isDisponibleNow()){
                System.out.println("Error en reserva: la mesa está ocupada");
                return "Error en reserva: la mesa está ocupada";
            }
            
            //Ver usuario que hace la reserva
            HttpSession sesion = request.getSession();
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");
            if(usuario == null){
                return "Error, el usuario no ha iniciado sesion";
            }
            
            //Ver si el usuario tiene ya una reserva activa
            ServletContext contextoApp = sesion.getServletContext();
            HashMap<Usuario, Reserva> reservasMap = (HashMap<Usuario, Reserva>) contextoApp.getAttribute("reservasActivas");
            if(reservasMap == null){
                reservasMap = new HashMap();
            }
            if(reservasMap.containsKey(usuario)){
                return "Error: ya esta sentado en una mesa";
            }
            
            if(!mesa.sentar(usuario, nextReservaId)){
                System.out.println("Error en la reserva: reservaId = -1");
                return "Error en la reserva";                
            } else{
                //RESERVA CORRECTA
                    Long millis = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli();             
                    
                    //Pasar el long a una hora String
                    LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
                    int horaInt = date.getHour();
                    int minutoInt = date.getMinute();
                    String horaParsed = Integer.toString(horaInt);
                    if(horaInt < 10){
                        horaParsed = "0" + horaParsed;
                    }
                    String minutoParsed = Integer.toString(minutoInt);
                    if(minutoInt < 10){
                        minutoParsed = "0" + minutoParsed;
                    }
                    String horaString = horaParsed + ":" + minutoParsed;
                    
                    //Añadir reserva
                    Reserva reserva = new Reserva(this.getId(), nextReservaId, mesa, millis, this.getNombre(), horaString);
                    reserva.addComensal(usuario);
                    nextReservaId++;
                    
                    //TODO AÑADIR OPCION DE SENTARSE EN MESA YA RESERVADA (POR EL MISMO USUARIO) (SACAR DE LAS RESERVAS DEL PROPIO RESTAURANTE)
                    reservas.add(reserva);
                    reservasActivas.add(reserva);
                    
                    Gson gson = new Gson();
                    message = gson.toJson(reserva);
            }
            
        } catch(NumberFormatException nfe){
            System.out.println("El id no es un int parseable\n"+ nfe);
            message = "Error: El id no es un int parseable";
            return message;
        }
        
        return message;   
    }
    
    public String sentarEnReservada(HttpServletRequest request){
        String message;
        //int result = gestor.reservarMesa(mesa, usuario, LocalDateTime.MAX);
        try{
             //Encuentra la reserva 
            int reservaId = Integer.parseInt(request.getParameter("reservaId"));
            Reserva reserva = findReserva(new Reserva(this.getId(), reservaId));
            if(reserva == null){
                System.out.println("Error: La reserva con reserva id " + reservaId + " no existe en el restaurante " + this.nombre);
                message = "Error: La reserva con reserva id " + reservaId + " no existe en el restaurante " + this.nombre;
                return message;
            }
            
            //Es una reserva del usuario?
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            if(usuario == null){
                message = "Error: no se ha encontrado el usuario";
                System.out.println(message);
                return message;
            }
            if(!reserva.isComensal(usuario)){
                message = "Error: esta reserva no se corresponde con el usuario";
                System.out.println(message);
                return message;
            }            
            //Ver si el usuario tiene ya una reserva activa
            ServletContext contextoApp = request.getSession().getServletContext();
            HashMap<Usuario, Reserva> reservasMap = (HashMap<Usuario, Reserva>) contextoApp.getAttribute("reservasActivas");
            if(reservasMap == null){
                reservasMap = new HashMap();
            }
            if(reservasMap.containsKey(usuario)){
                return "Error: ya esta sentado en una mesa";
            }
            
            //Comprobar la reserva a esa hora en la mesa
            Mesa mesa = reserva.getMesa();
            if(mesa.getIdReservaAt(LocalDateTime.now()) != reservaId){
                return "Error: No se ha encontrado la reserva en la mesa!";
            }
            
            //RESERVA ENCONTRADA--> ACTIVAR RESERVA
            message = "Usuario sentado correctamente";
            reservasActivas.add(reserva);
            //No hay que hacer nada en las mesas ya que no hay diferencia entre
            //reservar y sentarse, y la mesa ya esta reservada           
            
        } catch(NumberFormatException nfe){
            System.out.println("El id no es un int parseable\n"+ nfe);
            message = "Error: El id no es un int parseable";
            return message;
        }
        
        return message;   
    }
    
    
    
    public String levantarDeMesa(HttpServletRequest request){
        String message;
        
        try{
            //Encuentra la reserva 
            int reservaId = Integer.parseInt(request.getParameter("reservaId"));
            Reserva reserva = findReserva(new Reserva(this.getId(), reservaId));
            if(reserva == null){
                System.out.println("La reserva con reserva id " + reservaId + " no existe en el restaurante " + this.nombre);
                message = "Error: La reserva con reserva id " + reservaId + " no existe en el restaurante " + this.nombre;
                return message;
            }
            //Es una reserva activada ya?
            if(findActiva(reserva) == null){
                System.out.println("La reserva con reserva id " + reservaId + " en el restaurante " + this.nombre + " no esta activada: El usuario no  se ha sentado");
                message = "Error: La reserva con reserva id " + reservaId + " en el restaurante " + this.nombre + " no esta activada: El usuario no  se ha sentado";
                return message;
            }
            
                        
            Mesa mesa = reserva.getMesa();
            //Esta ocupada?
            if(mesa.isDisponibleNow()){
                System.out.println("Error en reserva: la mesa no está ocupada");
                return "Error en reserva: la mesa no está ocupada";
            }
            
            //Ver usuario que hace la reserva
            HttpSession sesion = request.getSession();
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");
            if(usuario == null){
                return "Error, el usuario no ha iniciado sesion";
            }
            
            //Comprobar que el usuario esta en la reserva
            if(!mesa.getComensales().contains(usuario)){
                System.out.println("Esta reserva no se corresponde con este usuario");
                return "Error: Esta reserva no se corresponde con este usuario";
            } else{
                //RESERVA ENCONTRADA
                mesa.levantar(usuario, reservaId);
                message = "Usuario levantado correctamente";
                
                removeReserva(reserva);
                removeActiva(reserva);
            }
            
        } catch(NumberFormatException nfe){
            System.out.println("El id no es un int parseable\n"+ nfe);
            message = "Error: El id no es un int parseable";
            return message;
        }
        
        return message;   
    }
    
    public String cancelarReserva(HttpServletRequest request){
        String message;
        
        String reservaIdString = request.getParameter("reservaId");
        if(reservaIdString == null){
            System.out.println("Error: No hay una reserva seleccionada");
            return "Error: No hay una reserva seleccionada";
        }
        try {
            
            int reservaId = Integer.parseInt(reservaIdString);
            Reserva reserva = findReserva(new Reserva(this.getId(), reservaId));
            if(reserva == null){
                message = "Error: No se ha encontrado la reserva " + reservaIdString + " en el restaurante " + this.getId();
                System.out.println(message);
                return message;
            }
            //Es una reserva del usuario?
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            if(usuario == null){
                message = "Error: no se ha encontrado el usuario";
                System.out.println(message);
                return message;
            }
            if(!reserva.isComensal(usuario)){
                message = "Error: esta reserva no se corresponde con el usuario";
                System.out.println(message);
                return message;
            }
            
            //Es una reserva activada ya?
            if(findActiva(reserva) != null){
                message = "Error: La reserva con reserva id " + reservaId + " en el restaurante " + this.nombre + " ya esta activada: El usuario no puede cancelarla";
                System.out.println(message);
                return message;
            }
            
            
            //RESERVA ENCONTRADA Y NO ACTIVADA: SE PUEDE CANCELAR
            Mesa mesaReserva = reserva.getMesa();
            mesaReserva.cancelarReserva(reservaId);
            
            removeReserva(reserva);
            message = "Reserva cancelada correctamente!";
            
        } catch(NumberFormatException nfe){
            message = "Error: El id de reserva no es un int parseable" + nfe;
            System.out.println(message);
            return message;
        }
        
        return message;   
        
        
    }
    
    public String mostrarMesas(){
        if(mesas.isEmpty()){
            try {
                RestauranteDAO dao = new RestauranteDAO();
                mesas.addAll(dao.findAllMesasFromRestaurante(id));
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(GestorMesa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Gson gson = new Gson();
        String result = gson.toJson(mesas);
        
        return result;
    }
    
    public String mostrarHorasDisponibles(HttpServletRequest request){
        String message;
        
        try{
            //Encuentra la mesa a reservar 
            int mesaID = Integer.parseInt(request.getParameter("mesaId"));
            Mesa mesa = findMesa(mesaID);
            if(mesa == null){
                System.out.println("La mesa con id " + mesaID + " no existe");
                message = "Error: No se ha encontrado una mesa con ese id";
                return message;
            }
            
            List<String> lista = mesa.getHorasLibres();
            Gson gson = new Gson();
            message = gson.toJson(lista);
            
        }catch(NumberFormatException nfe){
            System.out.println("El id no es un int parseable\n"+ nfe);
            message = "Error: El id no es un int parseable";
            return message;
        }
        
        
        return message;
    }
     
    public Mesa findMesa(int id){
        Mesa result = null;
        for (Mesa next : mesas) {
            if(id == next.getId()){
                result= next;
                break;
            }
        }
        if(result.getHorario() != null)
            System.out.println("El horario de la mesa " + result.getId() + " es : " + result.getHorario());
        else
            System.out.println("La mesa no tiene horario!");
        
        return result;
    }
      
    public String mesaToString(int mesaId){
        return mesas.get(mesaId).toString();
    }
    
    public void addMesa(Mesa mesa){
        this.mesas.add(mesa);
    }
    
    public void removeMesa(Mesa mesa){
        this.mesas.remove(mesa);
    }
    
    public void clearMesas(){
        this.mesas.clear();
    }
    
    public void addReserva(Reserva reserva){
        if(!reservas.contains(reserva))
            this.reservas.add(reserva);
    }
    
    public void removeReserva(Reserva reserva){
        this.reservas.remove(reserva);
    }
    
    public void clearReservas(){
        this.reservas.clear();
    }
    
    public void addActiva(Reserva reserva){
        if(!reservasActivas.contains(reserva))
            this.reservasActivas.add(reserva);
    }
    
    public void removeActiva(Reserva reserva){
        this.reservasActivas.remove(reserva);
    }
    
    public void clearActiva(){
        this.reservasActivas.clear();
    }
    
    //TODO: Programar persistencia de las mesas en la base de datos
    public Horario getHorario() {
        return horario;
    }
    
    public void setHorario(Horario horario) {
        this.horario = horario;
        setHorarioAllMesas(horario);
    }
    
    public void setHorarioAllMesas(Horario horario){
        for(Mesa mesa : mesas){
            mesa.setHorario(new Horario(horario));
        }
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
    
    public ArrayList<Mesa> getMesas() {
        return mesas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }
    
    @Override
    public int compareTo(GestorMesa o) {
        return this.id - o.getId();
    }

    public Reserva findReserva(Reserva reserva) {
        Reserva res = null;
        for(Reserva r : this.reservas){
            if(r.equals(reserva)){
                res = r;
                break;
            }             
            
        }
        return res;
    }
    
    public Reserva findActiva(Reserva reserva) {
        Reserva res = null;
        for(Reserva r : this.reservasActivas){
            if(r.equals(reserva)){
                res = r;
                break;
            }             
            
        }
        return res;
    }
    
    public Collection<Usuario> getAllUsuariosActuales(){
        /**
         * Devuelve todos los usuarios que hay sentados ahora mismo en las mesas
         */
        HashSet<Usuario> set = new HashSet();
        for(Mesa mesa : this.mesas){
            for(Usuario user : mesa.getComensales()){
                set.add(user);
            }
        }
        
        return set;
    }
}
