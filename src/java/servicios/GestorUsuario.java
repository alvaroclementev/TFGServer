/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import dominio.Usuario;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.gson.Gson;
import dao.UsuarioDAO;
import dominio.EstadoLogin;
import dominio.Reserva;
import dominio.Restaurante;
import dominio.exceptions.UserAlreadyExistsException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Alvaro
 */
public class GestorUsuario {
    
    private static final String SERVER_CLIENT_ID = "318219251312-j29fp4417qqhav7c5s893m7vbl2ltb0c.apps.googleusercontent.com";
    
    public static String loginGoogle(HttpServletRequest request){
        
        String result = "Error: login invalido";
        Usuario usuario = null;
        //Mirar primero si esta ya logeado desde esa sesion
        HttpSession sesion = request.getSession();
        boolean logged = (Boolean.valueOf((String) sesion.getAttribute("logged")));
        if(logged){
            //TODO GESTIONAR MULTIPLES LOGINS, PROBABLEMENTE HAYA QUE TENER UNA LISTA CON LOS USUARIOS LOGEADOS, O SE PERMITE LOGIN DESDE MULTIPLES DISPOSITIVOS?
            result = "Error: ya logeado";
        }
        else{
            String idTokenString = request.getParameter("id_token");
            NetHttpTransport transport = new NetHttpTransport();
            GsonFactory jsonFactory = new GsonFactory();

            if(idTokenString != null && !idTokenString.equals("")){
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                        .setAudience(Arrays.asList(SERVER_CLIENT_ID))
                        .setIssuer("https://accounts.google.com").build();

                try{
                    GoogleIdToken idToken = verifier.verify(idTokenString);
                    if(idToken != null){
                        Payload payload = idToken.getPayload();
                        String email = payload.getEmail();

                        UsuarioDAO dao = new UsuarioDAO();
                        if(dao.existsUsuario(email)){
                            usuario = dao.findUsuarioByEmail(email);
                        }
                    }

                    if(usuario != null){
                        sesion.setAttribute("logged", true);
                        sesion.setAttribute("usuario", usuario);
                        System.out.println("El usuario " + usuario.getEmail() + " ha iniciado sesión");
                        
                        //Generar y devolver el estado inicial del usuario
                        EstadoLogin estado = generarEstado(usuario, request.getServletContext());
                        Gson gson = new Gson();
                        result = gson.toJson(estado);
                        Logger.getLogger(GestorUsuario.class.getName()).log(Level.INFO, "El resultado es: " + result);
                        System.out.println("El estado del usuario " + usuario.getEmail() + " es " + result);
                        return result;
                    }

                } catch (IOException | GeneralSecurityException ex) {
                    Logger.getLogger(GestorUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(GestorUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GestorUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return result;       
    }
    
    public static String registrarGoogle(HttpServletRequest request){
        String result = "Error en registro";
        Usuario usuario = null;
        String idTokenString = request.getParameter("id_token");
        NetHttpTransport transport = new NetHttpTransport();
        GsonFactory jsonFactory = new GsonFactory();

        if(idTokenString != null && !idTokenString.equals("")){
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Arrays.asList(SERVER_CLIENT_ID))
                    .setIssuer("https://accounts.google.com").build();
            
            try{
                GoogleIdToken idToken = verifier.verify(idTokenString);
                if(idToken != null){
                    Payload payload = idToken.getPayload();
                    String email = payload.getEmail();
                    String nombre = (String) payload.get("given_name");
                    String apellidos = (String) payload.get("family_name");
                    
                    usuario = new Usuario(email, nombre, apellidos);
                    UsuarioDAO dao = new UsuarioDAO();
                    if(dao.existsUsuario(email)){
                        Exception e = new UserAlreadyExistsException(email);
                        result = e.toString();
                        System.out.println(result);
                    }
                    else{
                        dao.addUsuario(usuario);
                        HttpSession sesion = request.getSession();
                        sesion.setAttribute("logged", true);
                        sesion.setAttribute("usuario", usuario);
                        System.out.println("El usuario " + usuario.getEmail() + " ha iniciado sesión");
                        
                        
                        //Generar y devolver el estado inicial del usuario
                        EstadoLogin estado = generarEstado(usuario, request.getServletContext());
                        Gson gson = new Gson();
                        result = gson.toJson(estado);
                        //Logger.getLogger(GestorUsuario.class.getName()).log(Level.INFO, "El resultado es: " + result);
                        System.out.println("El estado del usuario " + usuario.getEmail() + " es " + result);
                        return result;
                    }
                }
               
            } catch (IOException | GeneralSecurityException ex) {
                Logger.getLogger(GestorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GestorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GestorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;       
    }
    
    public static String login(HttpServletRequest request){
        String result = "Error en login";
        try {            
            //Check usuario y contraseña
            String usuarioString = request.getParameter("usuario");
            String password = request.getParameter("password");
            if(usuarioString == null || password == null){
                System.out.println("Error: usuario o contraseña vacio");
                return "Error: usuario o contraseña vacio";
            }
            
            //Ver si usuario existe
            UsuarioDAO dao = new UsuarioDAO();
            if(!dao.existsUsuario(usuarioString)){
                System.out.println("Error: el usuario no existe");
                return "Error: el usuario no existe";
            }
            
            //Comprobar contraseña
            if(!dao.checkPassword(usuarioString, password)){
                System.out.println("Error: la contraseña no es correcta");
                return "Error: La contraseña no es correcta";
            }
            else{
                Usuario usuario = dao.findUsuarioByEmail(usuarioString);
                HttpSession sesion = request.getSession();
                sesion.setAttribute("logged", true);
                sesion.setAttribute("usuario", usuario);
                System.out.println("El usuario " + usuario.getEmail() + " ha iniciado sesión");
                
                //Generar y devolver el estado inicial del usuario
                EstadoLogin estado = generarEstado(usuario, request.getServletContext());
                Gson gson = new Gson();
                result = gson.toJson(estado);
                Logger.getLogger(GestorUsuario.class.getName()).log(Level.INFO, "El resultado es: " + result);
                System.out.println("El estado del usuario " + usuario.getEmail() + " es " + result);
                return result;
            }
            
        } catch (ClassNotFoundException | SQLException ex ) {
            Logger.getLogger(GestorUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static String registrar(HttpServletRequest request){
        String result = "Error en registro";
        try {            
            //Check usuario y contraseña
            String usuarioString = request.getParameter("usuario");
            String password = request.getParameter("password");
            if(usuarioString == null || password == null){
                System.out.println("Error: usuario o contraseña vacio");
                return "Error: usuario o contraseña vacio";
            }
            
            //Ver si usuario existe
            UsuarioDAO dao = new UsuarioDAO();
            if(dao.existsUsuario(usuarioString)){
                Exception e = new UserAlreadyExistsException(usuarioString);
                System.out.println(e.toString());
                return e.toString();
            }           
            else{
                //EXITO: REGISTRANDO...
                String nombre = request.getParameter("nombre");
                String apellidos = request.getParameter("apellidos");
                
                Usuario usuario = new Usuario(usuarioString, nombre, apellidos);
                dao.addUsuario(usuario, password);
                
                HttpSession sesion = request.getSession();
                sesion.setAttribute("logged", true);
                sesion.setAttribute("usuario", usuario);
                System.out.println("Se ha registrado el usuario " + usuario);
                System.out.println("El usuario " + usuario.getEmail() + " ha iniciado sesión");
                
                
                //Generar y devolver el estado inicial del usuario
                EstadoLogin estado = generarEstado(usuario, request.getServletContext());
                Gson gson = new Gson();
                result = gson.toJson(estado);
                Logger.getLogger(GestorUsuario.class.getName()).log(Level.INFO, "El resultado es: " + result);
                System.out.println("El estado del usuario " + usuario.getEmail() + " es " + result);
                return result;
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(GestorUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static String logout(HttpServletRequest request){
        String result = "error en logout";
        HttpSession sesion = request.getSession();
        boolean logged = (boolean) sesion.getAttribute("logged");
        if(logged){
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");
            if(usuario != null){
                sesion.setAttribute("logged", false);
                sesion.setAttribute("usuario", null);
                
                result = "sesion cerrada correctamente";
                System.out.println("El usuario " + usuario.getEmail() + " ha cerrado sesión");
            }
        }
        else{
            result = "no hay una sesion iniciada en esta conexion";
        }
        return result;
    }
    
    public static EstadoLogin generarEstado(Usuario usuario, ServletContext contextoApp){
        Collection<Reserva> allReservas = new TreeSet();
        Restaurante restauranteActivo = null;
        Reserva reservaActiva = null;
        //Se recorren todos los restaurantes en busca de las reservas del usuario:
        Collection<Restaurante> allRestaurantes = GestorRestaurante.findAllRestaurantes(contextoApp);
        for(Restaurante restaurante : allRestaurantes){
            allReservas.addAll(restaurante.getReservasFromUsuario(usuario));
            Reserva activaRestaurante = restaurante.getReservaActivaFromUsuario(usuario);
            if( activaRestaurante!= null){
                if(reservaActiva == null){
                    reservaActiva = activaRestaurante;
                    restauranteActivo = restaurante;
                }
                else{
                    System.out.println("Error: El usuario tiene dos reservas activas en dos restaurantes distintos? \nReserva 1 = " + reservaActiva + "\nReserva2 = " + activaRestaurante);
                    return null;
                }   
            }
        }
        //Ya se tienen todas las reservas, se crea el estado
        EstadoLogin estado = new EstadoLogin(reservaActiva, allReservas, restauranteActivo);
        return estado;
    }
}
