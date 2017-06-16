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
import dao.UsuarioDAO;
import dominio.exceptions.UserAlreadyExistsException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Alvaro
 */
public class GestorUsuario {
    
    private static final String SERVER_CLIENT_ID = "318219251312-j29fp4417qqhav7c5s893m7vbl2ltb0c.apps.googleusercontent.com";
    
    public static String loginGoogle(HttpServletRequest request){
        
        String result = "ID token invalido";
        Usuario usuario = null;
        //Mirar primero si esta ya logeado desde esa sesion
        HttpSession sesion = request.getSession();
        boolean logged = (Boolean.valueOf((String) sesion.getAttribute("logged")));
        if(logged){
            //TODO GESTIONAR MULTIPLES LOGINS, PROBABLEMENTE HAYA QUE TENER UNA LISTA CON LOS USUARIOS LOGEADOS, O SE PERMITE LOGIN DESDE MULTIPLES DISPOSITIVOS?
            result = "ya logeado";
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
                        result = "login OK";
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
        String result = "error en registro";
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
                        result = "registro completado, login ok";
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
            }
        }
        else{
            result = "no hay una sesion iniciada en esta conexion";
        }
        return result;
    }
}
