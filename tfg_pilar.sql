-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.7.18-log - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para tfg
DROP DATABASE IF EXISTS `tfg`;
CREATE DATABASE IF NOT EXISTS `tfg` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `tfg`;

-- Volcando estructura para tabla tfg.mesas
DROP TABLE IF EXISTS `mesas`;
CREATE TABLE IF NOT EXISTS `mesas` (
  `id` int(11) NOT NULL,
  `restaurante` int(11) NOT NULL,
  `maxComensales` int(11) NOT NULL,
  PRIMARY KEY (`id`,`restaurante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla tfg.mesas: ~28 rows (aproximadamente)
/*!40000 ALTER TABLE `mesas` DISABLE KEYS */;
REPLACE INTO `mesas` (`id`, `restaurante`, `maxComensales`) VALUES
	(1, 1, 3),
	(1, 2, 4),
	(1, 3, 4),
	(1, 4, 2),
	(1, 5, 4),
	(1, 6, 2),
	(1, 7, 8),
	(2, 1, 2),
	(2, 2, 2),
	(2, 3, 4),
	(2, 4, 4),
	(2, 5, 3),
	(2, 6, 4),
	(2, 7, 2),
	(3, 1, 3),
	(3, 2, 4),
	(3, 3, 2),
	(3, 4, 4),
	(4, 1, 5),
	(4, 2, 5),
	(4, 3, 4),
	(4, 4, 4),
	(5, 1, 6),
	(5, 2, 2),
	(5, 3, 8),
	(5, 4, 6),
	(6, 1, 4),
	(6, 2, 2);
/*!40000 ALTER TABLE `mesas` ENABLE KEYS */;

-- Volcando estructura para tabla tfg.productos
DROP TABLE IF EXISTS `productos`;
CREATE TABLE IF NOT EXISTS `productos` (
  `id` int(10) unsigned NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `precio` float unsigned DEFAULT '0',
  `restauranteId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`,`restauranteId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla tfg.productos: ~27 rows (aproximadamente)
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
REPLACE INTO `productos` (`id`, `nombre`, `descripcion`, `precio`, `restauranteId`) VALUES
	(1, 'Rollitos Vietnamitas', 'Rellenos de carne picada, verduritas y noodles', 5.6, 4),
	(2, 'Harumaki de Langostinos', 'Rollitos dorados en su punto con setas shiitake y bambú', 5.9, 4),
	(3, 'Alitas de Pollo Crujientes', 'Caramelizadas con un toque de miel y sésamo', 6.2, 4),
	(4, 'Yakitori', 'Brochetas de Pollo teriyaki/ solomillo con foie/ langostinos con piña', 6.5, 4),
	(5, 'Tempura de Helado', 'Con salsa de fruta de la pasión', 5.5, 4),
	(6, 'Lemon Cake', 'Ácida y dulce, un final feliz', 5.5, 4),
	(7, 'Coca-Cola', '33 cl', 1.5, 4),
	(8, 'Nuestras croquetas', 'De jamón ibérico, chipirones o boletus', 9.5, 2),
	(9, 'Tartar de atún con aguacate', 'Atún rojo de la Almadraba ', 16, 2),
	(10, 'Lubina a la plancha', 'Con tomate cherry y verduritas salteadas', 16.5, 2),
	(11, 'Pluma Ibérica', 'Acompañado con puré de berenjena y salsa de higos', 15, 2),
	(12, 'Lomo de buey', 'A tu gusto y con patata rota', 16, 2),
	(13, 'Lasaña de rabo de toro', 'También en versión sin gluten ', 14.5, 2),
	(14, 'Ravioli de trufa', 'En una exquisita salsa de boletus', 12, 2),
	(15, 'Guacamole', 'Se acompaña con totopos de maíz', 6.95, 3),
	(16, 'Quesadillas Si Señor', '3 tortillas de harina de trigo rellenas de queso fundido,se acompañan de guacamole,frijol refrito y crema agria.\r\n', 7.35, 3),
	(17, 'Flautas de pollo', '3 taquitos de pollo dorados y cubiertos de lechuga, crema agria y queso fresco', 8.85, 3),
	(18, 'Tacos carnitas', 'Pierna de cerdo asada, cebolla, cilantro, salsa verde y tortillas de maíz.', 12.9, 3),
	(19, 'Volcanes ', '3 tortillas de maíz fritas con frijoles, ternera, cebolla, cilantro y queso gratinado.', 11.9, 3),
	(20, 'Crepa de cajeta ', 'Crepa bañada en dulce de leche. Se sirve con nuez y helado.', 5.8, 3),
	(21, 'Bastones de berenjena', 'Aderezada con miel de flores', 11.5, 1),
	(22, 'Pulpo braseado', 'Acompañado de patata asada y aceituna negra', 13.5, 1),
	(23, 'Bocata de calamares', 'Especialidad de la casa', 12, 1),
	(24, 'Ensaladilla rusa', 'La nuestra, original y divertida', 9.5, 1),
	(25, 'Roastbeef en su jugo', 'con puré de zanahoria y patatatitas ', 16.9, 1),
	(26, 'Chipirones al wok', 'Una ración de cous cous de acompañamiento', 11.5, 1),
	(27, 'Mini Hamburguesas ', '4 unidades con foie y Pedro Ximenez', 12.5, 1);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;

-- Volcando estructura para tabla tfg.restaurantes
DROP TABLE IF EXISTS `restaurantes`;
CREATE TABLE IF NOT EXISTS `restaurantes` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `descripcion` text,
  `categoria` varchar(50) NOT NULL DEFAULT 'Otros',
  `horaInicio` varchar(50) NOT NULL DEFAULT '12:00',
  `horaFin` varchar(50) NOT NULL DEFAULT '23:30',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla tfg.restaurantes: ~7 rows (aproximadamente)
/*!40000 ALTER TABLE `restaurantes` DISABLE KEYS */;
REPLACE INTO `restaurantes` (`id`, `nombre`, `direccion`, `telefono`, `descripcion`, `categoria`, `horaInicio`, `horaFin`) VALUES
	(1, 'El Columpio', 'C/ Caracas 10', '913787512', 'Colorido y luminoso restaurante mediterráneo, con muebles vintage y plantas, donde también sirven cócteles.', 'Español', '9:00', '23:59'),
	(2, 'Pointer', 'C/ Marqués de la Ensenada 16', '910526928', 'Territorios desconocidos a partir de la excelencia culinaria; recogiendo lo mejor de cada rincón del mundo, logrando sabores reales y difíciles de conseguir a través de una gastronomía internacional.', 'Otros', '12:00', '23:30'),
	(3, 'Si Señor', 'C/ Paseo de la Castellana 128', '915618686', 'Cócteles, fajitas, quesadillas y otros platos mexicanos en una colorida cantina ambientada con luces de neón.', 'Otros', '8:00', '23:45'),
	(4, 'Restaurante 4', 'C/ Lagasca 5', '914319308', 'Restaurante japonés de decoración chic con platos innovadores, sushi bar y servicio de comida para llevar.', 'Asiático', '13:00', '23:30'),
	(5, 'Thai Garden ', 'C/ Arturo Soria 207', '915778884', 'Budas, orquídeas y velas decoran este chalé de aire colonial con terraza-jardín que sirve cocina tailandesa.', 'Asiático', '13:30', '23:30'),
	(6, 'O’hara’s Irish Pub & Restaurant', ' Vía de las Dos Castillas 23', '913521188', 'Hamburguesas, platos variados y copas en taberna irlandesa con deporte en pantalla grande y juegos de mesa.', 'Hamburguesería', '12:30', '23:59'),
	(7, 'Flavia', 'C/ Gil de Santivanes, 2', '914939051', 'Pizzas al horno de leña, pasta fresca y postres italianos en un sofisticado restaurante chic con coctelería.', 'Italiano', '8:00', '23:00');
/*!40000 ALTER TABLE `restaurantes` ENABLE KEYS */;

-- Volcando estructura para tabla tfg.usuarios
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `email` varchar(100) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `apellidos` varchar(200) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla tfg.usuarios: ~6 rows (aproximadamente)
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
REPLACE INTO `usuarios` (`email`, `nombre`, `apellidos`, `password`) VALUES
	('clemente.varo@gmail.com', 'Alvaro', 'Clemente Verdu', NULL),
	('clementealvaropruebas@gmail.com', 'Alvaro', 'Clemente', NULL),
	('test1', 'NombreLorem', 'Apellidos Ipsum', 'test1'),
	('usuariodeprueba', 'Prueba', 'De Prueba', '1234'),
	('usuariodeprueba2', 'Prueba2', 'PRUEBA', '1111'),
	('usuariodeprueba3', NULL, NULL, '3333');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
