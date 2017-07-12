/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import dao.ProductoDAO;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import servicios.GestorMesa;
import servicios.GestorPedido;
import servlets.ControllerPedidos;

/**
 *  Esta clase representa un restaurante
 *  Es la clase básica encargada de dar información sobre un restaurante y de hacer
 *  de interfaz con algunos gestores.
 * @author Alvaro
 */
public class Restaurante implements Comparable<Restaurante>{
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String descripcion;
    private String categoria;
    private String horaInicio;
    private String horaFin;
    
    //Tener cacheado todos los productos
    private TreeSet<Producto> productos;
    
    private int nextPedidoId = 1;
    
    //Gestores no serializables para GSON
    @Expose(serialize = false)
    private GestorMesa gestorMesa;
        
    //Historial no serializable para GSON
    @Expose(serialize = false)
    private final Collection<Pedido> historialPedidos = new HashSet(); //Historial de pedidos procesados por el restaurante

    public Restaurante(int id, String nombre, String direccion, String telefono, String descripcion, String categoria, String horaInicio, String horaFin, Set<Producto> productos, GestorMesa gestorMesa, GestorPedido gestorPedido) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.productos = new TreeSet();
        this.productos.addAll(productos);
        
        this.gestorMesa = gestorMesa;
    }

    public Restaurante(int id, String nombre, String direccion, String telefono, String descripcion, String categoria, String horaInicio, String horaFin) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        
        this.productos = new TreeSet();
        initGestores();
    }

    public Restaurante(int id) {
        this.id = id;
    }   
    
    //Funciones del restaurante
    public String mostrarProductos(){
        if(this.productos.isEmpty()){
            //Si no esta cacheado sacalo de memoria
            try {
                this.productos = new TreeSet();
                ProductoDAO dao = new ProductoDAO();
                this.productos.addAll(dao.findAllProductos(this.id));
                
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(GestorMesa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Gson gson = new Gson();
        String result = gson.toJson(this.productos, TreeSet.class);
        
        return result;
    }
    
     public String hacerPedido(HttpServletRequest request){
        /**
         * En este paso el restaurante se encarga de encontrar la reserva
         * la reserva hace el resto
         */
        String reservaIdRequest = request.getParameter("reservaId");
        if(reservaIdRequest == null){
            return "Error: no hay una id de reserva";
        }
        try{
            //Encontrar la reserva
            int idReserva = Integer.parseInt(reservaIdRequest);
            Reserva reserva = gestorMesa.findReserva(new Reserva(this.getId(), idReserva));
            if(reserva == null){
                return "Error: no se ha encontrado la reserva";
            }
            //Es una reserva activa?
            if(gestorMesa.findActiva(reserva) == null){
                return "Error: la reserva no esta activada";
            }
            //SE HA ENCONTRADO LA RESERVA Y ES ACTIVA
            return reserva.hacerPedido(request, this);
        } catch(NumberFormatException nfe){
            Logger.getLogger(ControllerPedidos.class.getName()).log(Level.SEVERE, null, nfe);
            return "Error: no se ha podido parsear la id de la mesa";
        }
    }
    
    public String checkout(HttpServletRequest request){
        /**
         * En este paso el restaurante se encarga de encontrar la reserva
         * la reserva hace el resto
         */
        String reservaIdRequest = request.getParameter("reservaId");
        if(reservaIdRequest == null){
            return "Error: no hay una id de reserva";
        }
        try{
            //Encontrar la reserva
            int idReserva = Integer.parseInt(reservaIdRequest);
            Reserva reserva = gestorMesa.findReserva(new Reserva(this.getId(), idReserva));
            if(reserva == null){
                return "Error: no se ha encontrado la reserva";
            }
            //SE HA ENCONTRADO LA RESERVA!
            return reserva.checkout(request, this);
        } catch(NumberFormatException nfe){
            Logger.getLogger(ControllerPedidos.class.getName()).log(Level.SEVERE, null, nfe);
            return "Error: no se ha podido parsear la id de la mesa";
        }
    }
    
    public String pedidoParaReserva(HttpServletRequest request){
       /**
         * En este paso el restaurante se encarga de encontrar la reserva
         * la reserva hace el resto
         */
        String reservaIdRequest = request.getParameter("reservaId");
        if(reservaIdRequest == null){
            return "Error: no hay una id de reserva";
        }
        try{
            //Encontrar la reserva
            int idReserva = Integer.parseInt(reservaIdRequest);
            Reserva reserva = gestorMesa.findReserva(new Reserva(this.getId(), idReserva));
            if(reserva == null){
                return "Error: no se ha encontrado la reserva";
            }
            if(gestorMesa.findActiva(reserva) != null){
                return "Error: la reserva ya esta activada";
            }
            
            //SE HA ENCONTRADO LA RESERVA Y NO ESTA ACTIVA! 
            return reserva.pedidoFuturo(request, this);
        } catch(NumberFormatException nfe){
            Logger.getLogger(ControllerPedidos.class.getName()).log(Level.SEVERE, null, nfe);
            return "Error: no se ha podido parsear la id de la mesa";
        }
        
    }
    
    public String solicitarCamarero(HttpServletRequest request){
        System.out.println("Restaurante " +  this.getId() + ": ");
        return gestorMesa.solicitarCamarero(request);
    }
    
    public int getNextPedidoId(){
        int res = nextPedidoId;
        nextPedidoId++;
        return res;
    }
    
    //Para el panel de control
    public String mostrarReservas(){
        Collection<Reserva> reservas = this.gestorMesa.getReservas();
                
        Gson gson = new Gson();
        return gson.toJson(reservas);
    }
    
    public String mostrarReservasActivas(){
        Collection<Reserva> reservasActivas = this.gestorMesa.getReservasActivas();
                
        Gson gson = new Gson();
        return gson.toJson(reservasActivas);
    }
    
    public String mostrarPedidos(){
        Collection<Reserva> reservas = this.gestorMesa.getReservas();
        Collection<Pedido> pedidos = new TreeSet();
        for(Reserva reserva: reservas){
            pedidos.addAll(reserva.getPedidosAntemano());
        }
        Gson gson = new Gson();
        return gson.toJson(pedidos);
    }
    
    public String mostrarSolicitudesCamarero(){
        Collection<Integer> solicitudes = this.gestorMesa.getSolicitudesCamarero();
        Gson gson = new Gson();
        return gson.toJson(solicitudes);
    }
    
    //Funciones de interfaz con los gestores
    private void initGestores(){
        this.gestorMesa = new GestorMesa(this.id, horaInicio, horaFin, 10);
    }
    
    public String reservarMesa(HttpServletRequest request){
        return this.gestorMesa.reservarMesa(request, this);
    }
    
    public String sentarEnMesa(HttpServletRequest request){
        return this.gestorMesa.sentarEnMesa(request, this);
    }
    
    public String sentarEnReservada(HttpServletRequest request){
        return this.gestorMesa.sentarEnReservada(request, this);
    }
    
    public String levantarDeMesa(HttpServletRequest request){
        return this.gestorMesa.levantarDeMesa(request, this);
    }
    
    public String cancelarReserva(HttpServletRequest request){
        return this.gestorMesa.cancelarReserva(request, this);
    }
    
    public String mostrarMesas(){
        return this.gestorMesa.mostrarMesas(this);
    }
    
    public String mostrarHorasDisponibles(HttpServletRequest request){
        return this.gestorMesa.mostrarHorasDisponibles(request);
    }
    
    public Mesa findMesa(int id){
        return this.gestorMesa.findMesa(id);
    }
    
    public Collection<Reserva> getReservasFromUsuario(Usuario usuario){
        return this.gestorMesa.getReservasFromUsuario(usuario);
    }
    
    public Reserva getReservaActivaFromUsuario(Usuario usuario){
        return this.gestorMesa.getReservaActivaFromUsuario(usuario);
    }
    
    //Funciones del historial de pedidos

    public Collection<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }
    
    public void addToHistorial(Pedido pedido){
        if(historialPedidos.contains(pedido)){
            System.out.println("El historial ya contiene ese pedido!?");
        }
        else{
            historialPedidos.add(pedido);
        }
    }
    
    public void clearHistorial(){
        historialPedidos.clear();
    }
    
    public Pedido getFromHistorial(Pedido pedido){
        for(Pedido p : this.historialPedidos){
            if(pedido.equals(p)){
                return p;
            }
        }
        return null;
    }
    
    //Getters & Setters y otras funciones tipicas
    
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
    
    public GestorMesa getGestorMesa() {
        return gestorMesa;
    }

    public void setGestorMesa(GestorMesa gestorMesa) {
        this.gestorMesa = gestorMesa;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.id;
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
        final Restaurante other = (Restaurante) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Restaurante{" + "id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono + ", descripcion=" + descripcion + ", categoria=" + categoria + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + ", productos=" + productos + '}';
    }

    
    @Override
    public int compareTo(Restaurante o) {
        return this.id - o.getId();
    }
      
}
