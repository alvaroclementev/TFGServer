<%-- 
    Document   : panelcontrol
    Created on : 09-jul-2017, 14:21:06
    Author     : Alvaro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <title>Panel de Control</title>
        <script src="js/jquery-3.1.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/control.js"></script>
    </head>
    <body>
        <span id = "selectedRestauranteId" style="visibility: collapse">${restaurante.id}</span>
        <div class="container">
            
            <div class="jumbotron">
                <h1>Panel de Control</h1>
                <h2>${restaurante.nombre}</h2>
            </div>
            <div style="margin-bottom: 50px">
                <h3 >Mesas</h3>
                <table id="tablaMesas" class="table table-striped">
                    <thead>
                        <th>Disponibles</th>
                        <th>Ocupadas</th>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            
            <div style="margin-bottom: 50px">
                <h3 >Reservas</h3>
                <table id="tablaReservas" class="table table-striped" >
                    <thead>
                        <th>ID</th>
                        <th>Mesa</th>
                        <th>Hora</th>
                        <th>Usuarios</th>
                        <th>Activa</th>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            
            <div>
                <h3 >Productos</h3>
                <table id="tablaPedidos" class="table table-striped">
                    <thead>
                        <th>Mesa</th>
                        <th>Hora</th>
                        <th>Productos</th>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
            
    <div id="myModal" class="modal fade" role="dialog">
        <div class="modal-dialog">

          <!-- Modal content-->
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h4 class="modal-title">Se necesita camarero</h4>
            </div>
            <div class="modal-body">
                <p>Se necesita un camarero en la mesa <span id="mesaModal"></span></p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
          </div>

        </div>
    </div>
    </body>
</html>
