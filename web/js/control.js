var selectedRestauranteId;

//Cuando se cargue la pagina, se hace el setup de actualizar
$(document).ready(function() {
	console.log("OnDocumentReady");
	setUpActualizar();
});


function setUpActualizar() {
	console.log("Se va a actualizar cada segundo");
	selectedRestauranteId = document.getElementById("selectedRestauranteId").innerHTML;
	console.log("El restaurante seleccionado es " + selectedRestauranteId);
	setInterval(function(){
	    actualizar();
	}, 1000);
}


function actualizar(){
	console.log("actualizando...");
	//Hacer peticiones
	$.ajax({
		type: "GET",
		url: "PaneldeControl?selectedRestauranteId=" + selectedRestauranteId + "&opcion=actualizar",
		dataType: "json",
		success: function(data){
			console.log("onActualizarSuccess");
			onActualizarSuccess(data);
		},
		error: (jqXHR, errorStatus) => console.log(errorStatus)
	});
}

function onActualizarSuccess(data){
	console.log(data)

	procesarMesas(data[0]);
	procesarReservas(data[1], data[2]);
	procesarPedidos(data[3]);
	procesarSolicitudes(data[4]);
	
}

function procesarMesas(mesas){
	//Extraer las libres
	var idLibres = mesas.reduce( (total, mesa) => {
		if(mesa.ocupada === false){
			total.push(mesa.id);
		}
		return total;
	}, []);

	//Extraer las ocupadas
	var idOcupadas = mesas.reduce( (total, mesa) => {
		if(mesa.ocupada === true){
			total.push(mesa.id);
		}
		return total;
	}, []);

	//Create new TBody
	var new_tbody = document.createElement('tbody');
	var old_tbody = document.getElementById('tablaMesas').getElementsByTagName('tbody')[0];

	llenarBodyMesas(new_tbody, idLibres, idOcupadas);

	old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
}


function llenarBodyMesas(tbody, libres, ocupadas){

	var max = Math.max(libres.length, ocupadas.length);
	
	for(i = 0; i < max; i++){

		var new_row = tbody.insertRow(tbody.rows.length);
		var celdaLibre = new_row.insertCell(0);
		var celdaOcupada = new_row.insertCell(1);
		//Si hay, meter una libre
		if(i < libres.length){
			celdaLibre.innerHTML = libres[i];	
		} else {
			celdaLibre.innerHTML = "-";
		}
		if(i < ocupadas.length){
			celdaOcupada.innerHTML = ocupadas[i];
		} else {
			celdaOcupada.innerHTML = "-";
		}
	}
}

function procesarReservas(reservas, activas) {
	//Create new TBody
	var new_tbody = document.createElement('tbody');
	var old_tbody = document.getElementById('tablaReservas').getElementsByTagName('tbody')[0];


	reservas.forEach((reserva) => {
		var new_row = new_tbody.insertRow(new_tbody.rows.length);
		var celdaId = new_row.insertCell(0);
		var celdaMesa = new_row.insertCell(1);
		var celdaHora = new_row.insertCell(2);
		var celdaUsuarios = new_row.insertCell(3);
		var celdaActiva = new_row.insertCell(4);

		celdaId.innerHTML = reserva.reservaId;
		celdaMesa.innerHTML = reserva.mesa.id;
		celdaHora.innerHTML = reserva.horaString;
		celdaUsuarios.innerHTML = reserva.comensales[0].email;

		if(isActiva(reserva, activas)){
			celdaActiva.innerHTML = "Sí";	
		} else {
			celdaActiva.innerHTML = "No";
		}
		
	});

	old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
}

function isActiva(reserva, activas) {

	for(i = 0; i<activas.length; i++){
		if(reserva.id === activas[i].id){
			return true;
		}
	}

	return false;
}

function procesarPedidos(pedidos) {
	//Create new TBody
	var new_tbody = document.createElement('tbody');
	var old_tbody = document.getElementById('tablaPedidos').getElementsByTagName('tbody')[0];

	pedidos.forEach((pedido) => {
		var new_row = new_tbody.insertRow(new_tbody.rows.length);
		var celdaMesa = new_row.insertCell(0);
		var celdaHora = new_row.insertCell(1);
		var celdaProductos = new_row.insertCell(2);
		
		celdaMesa.innerHTML = pedido.idMesa;
		if(pedido.hora == -1){
			celdaHora.innerHTML = "Inmediatamente";	
		} else {
			celdaHora.innerHTML = pedido.hora;	
		}
		

		var productoContent = pedido.productos.map((producto) => producto.nombre);

		celdaProductos.innerHTML = productoContent;
	});

	old_tbody.parentNode.replaceChild(new_tbody, old_tbody);

}

var mesasAvisadas = [];
function procesarSolicitudes(solicitudes) {
	console.log("solicitudes = " + solicitudes);
	//Consigue referencia al modal
	var modal = document.getElementById('myModal');	
	var mesasPorAvisar = [];
	if(solicitudes.length != 0){
		solicitudes.forEach((mesaId) => {
			mesasPorAvisar.push(mesaId);
		});

		var nuevosAvisos = mesasPorAvisar.reduce( (total, mesaId) => {
			if(mesasAvisadas.indexOf(mesaId) == -1){
				total.push(mesaId);
				mesasAvisadas.push(mesaId);
			}

			return total;
		}, []);
		console.log("Los nuevos avisos son " + nuevosAvisos);

		if(nuevosAvisos.length > 0){
			var texto = "";
			nuevosAvisos.forEach( (mesaId, index) => {
				if(index === 0){
					texto = texto.concat(mesaId);	
				} else {
					texto = texto.concat(", " + mesaId);	
				}
				
			});

			console.log("El texto es : " + texto);

			$("#mesaModal").html(texto);	
			$("#myModal").modal("show");
		}
	}
}
