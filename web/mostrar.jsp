<%-- 
    Document   : mostrar
    Created on : 15-feb-2017, 19:08:52
    Author     : Alvaro Clemente
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mostrar Productos</title>
    </head>
    <body>
        <h1>Resultado Busqueda</h1>
        <c:if test="${not empty set}">
            <table>
                <tr>
                    <td><center><strong>ID</strong></center></td>
                    <td><center><strong>Nombre</strong></center></td>
                    <td><center><strong>Descripcion</strong></center></td>
                    <td><center><strong>Precio</strong></center></td>
                </tr>
                <c:forEach var="producto" items="${set}">
                    <tr>
                        <td>${producto.id}</td>
                        <td>${producto.nombre}</td>
                        <td>${producto.descripcion}</td>
                        <td>${producto.precio}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>
