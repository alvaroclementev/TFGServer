<%-- 
    Document   : resultado
    Created on : 15-feb-2017, 14:47:59
    Author     : Alvaro Clemente
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resultado de la Busqueda</title>
    </head>
    <body>
        <h1>Resultado de la busqueda</h1>
        <div>
            <h2>Id: ${producto.id}</h2><br>
            <h2>Nombre: ${producto.nombre}</h2><br>
            <h2>Descripcion: ${producto.descripcion}</h2><br>
            <h2>Precio: ${producto.precio}</h2><br>
        </div>
    </body>
</html>
