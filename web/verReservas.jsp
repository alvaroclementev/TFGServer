<%-- 
    Document   : verReservas
    Created on : 12-jun-2017, 22:33:50
    Author     : Alvaro
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mostrar Reservas</title>
    </head>
    <body>
        <h1>Todas las reservas</h1>
        <c:if test="${not empty session.reservas}">
            <table>
                <tr>
                    <td><center><strong>Restaurante</strong></center></td>
                    <td><center><strong>Id Reserva</strong></center></td>
                    <td><center><strong>hora</strong></center></td>
                </tr>
                <c:forEach var="reserva" items="${session.reservas}">
                    <tr>
                        <td>${reserva.restaurante.id}</td>
                        <td>${reserva.id}</td>
                        <td>${reserva.hora}</td>                        
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        
    </body>
</html>
