/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import dominio.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import org.springframework.mock.web.MockHttpServletRequest;
import servicios.GestorMesa;


/**
 *
 * @author Alvaro
 */
public class PruebasReserva {
    public static void main(String[] args){
        /*Usuario user = new Usuario("email@deprueba.com");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime horaInicio = LocalDateTime.parse("01-06-2017 08:00", formatter);
        LocalDateTime horaFin = LocalDateTime.parse("01-06-2017 22:30", formatter);
        Horario horario = new Horario(horaInicio, horaFin, 10);
        
        GestorMesa gestor = new GestorMesa(0, "prueba1", null, null, null);
        for(int i = 0; i< 4; i++){
            gestor.addMesa(new Mesa(i,4, horario));
        }
        
        
        
        //Reservar 1 mesa comportamiento normal
        Mesa mesa = gestor.findMesa(0);
        System.out.println("El findMesa(0) da como resultado : "+ mesa.toString());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("mesaId", "0");
        request.getSession().setAttribute("usuario", user);
        
        System.out.println(request.getSession().getAttribute("usuario"));
        
        System.out.println("Antes de reservar: " + mesa.isDisponibleNow());
        System.out.println(gestor.sentarEnMesa(request));
        
        System.out.println(mesa.isDisponibleNow());
        
        System.out.println(gestor.sentarEnMesa(request));
        
        System.out.println(gestor.levantarDeMesa(request));
        //System.out.println(gestor.sentarEnMesa(request));
        
        ZonedDateTime horaReserva = LocalDateTime.of(2017,6,1, 12,0).atZone(ZoneId.systemDefault());
        ZonedDateTime horaReserva2 = LocalDateTime.of(2017,6,1, 15,0).atZone(ZoneId.systemDefault());
        //System.out.println("Current time: " + Long.toString(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        //System.out.println("Reservation time: " + Long.toString(horaReserva.toInstant().toEpochMilli()));
        //System.out.println("Start time: " + Long.toString(horaInicio.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        
        request.setParameter("hora", Long.toString(horaReserva.toInstant().toEpochMilli()));
        System.out.println(gestor.reservarMesa(request));
        request.setParameter("hora", Long.toString(horaReserva2.toInstant().toEpochMilli()));
        System.out.println(gestor.reservarMesa(request));
        System.out.println(mesa.isDisponibleAt(LocalDateTime.of(2017,6,1, 15,0)));
        System.out.println(gestor.levantarDeMesa(request));
        System.out.println(mesa.isDisponibleAt(LocalDateTime.of(2017,6,1, 15,0)));
        System.out.println(gestor.reservarMesa(request));
        LocalDate hoy = LocalDate.now();
        LocalTime horaInicio = LocalTime.of(12, 30);
        LocalTime horaFin = LocalTime.of(23, 30);
        LocalDateTime defaultInicio = LocalDateTime.of(hoy, horaInicio);
        LocalDateTime defaultFin = LocalDateTime.of(hoy, horaFin);
        System.out.println("Inicio : " + defaultInicio + " y el Fin:  " + defaultFin);*/
        //Pruebas del Horario para mostrar las disponibles
        LocalDate hoy = LocalDate.now();
        LocalTime horaInicio = LocalTime.of(12, 30);
        LocalTime horaFin = LocalTime.of(23, 30);
        LocalDateTime dateTimeInicio = LocalDateTime.of(hoy, horaInicio);
        LocalDateTime dateTimeFin = LocalDateTime.of(hoy, horaFin);
        Horario horario = new Horario(dateTimeInicio, dateTimeFin, 10);
        System.out.println("Antes: " + horario.getHorasLibres());
        
        LocalTime horaReserva = LocalTime.of(20,0);
        LocalDateTime timeReserva = LocalDateTime.of(hoy, horaReserva);
        horario.reservarAt(timeReserva, 1);
        
        List lista = horario.getHorasLibres();
        System.out.println("Despues: " + horario.getHorasLibres());
        horario.eliminarReserva(1);
        System.out.println("Eliminada: " + horario.getHorasLibres());
        horario.reservarAt(LocalDateTime.now(), 2);
        System.out.println("Despues: " + horario.getHorasLibres());
        
        
    }
}
