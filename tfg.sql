-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.7.17-log - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para tfg
CREATE DATABASE IF NOT EXISTS `tfg` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `tfg`;

-- Volcando estructura para tabla tfg.mesas
CREATE TABLE IF NOT EXISTS `mesas` (
  `id` int(11) NOT NULL,
  `restaurante` int(11) NOT NULL,
  `maxComensales` int(11) NOT NULL,
  PRIMARY KEY (`id`,`restaurante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla tfg.mesas: ~6 rows (aproximadamente)
/*!40000 ALTER TABLE `mesas` DISABLE KEYS */;
REPLACE INTO `mesas` (`id`, `restaurante`, `maxComensales`) VALUES
	(1, 1, 3),
	(1, 2, 4),
	(2, 1, 2),
	(2, 2, 2),
	(3, 1, 3),
	(3, 2, 4);
/*!40000 ALTER TABLE `mesas` ENABLE KEYS */;

-- Volcando estructura para tabla tfg.productos
CREATE TABLE IF NOT EXISTS `productos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `precio` float unsigned DEFAULT '0',
  `restauranteId` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla tfg.productos: ~8 rows (aproximadamente)
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
REPLACE INTO `productos` (`id`, `nombre`, `descripcion`, `precio`, `restauranteId`) VALUES
	(1, 'Rollitos Vietnamitas', 'Rellenos de carne picada, verduritas y noodles', 5.6, 1),
	(2, 'Harumaki de Langostinos', 'Rollitos dorados en su punto con setas shiitake y bambú', 5.9, 1),
	(3, 'Alitas de Pollo Crujientes', 'Caramelizadas con un toque de miel y sésamo', 6.2, 1),
	(4, 'Yakitori', 'Brochetas de Pollo teriyaki/ solomillo con foie/ langostinos con piña', 6.5, 1),
	(5, 'Tempura de Helado', 'Con salsa de fruta de la pasión', 5.5, 1),
	(6, 'Lemon Cake', 'ácida y dulce, un final feliz', 5.5, 1),
	(7, 'Coca-Cola', '33 cl', 1.5, 1),
	(8, 'prueba', 'Lorem Ipsum', 42.42, 1);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;

-- Volcando estructura para tabla tfg.restaurantes
CREATE TABLE IF NOT EXISTS `restaurantes` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `descripcion` text,
  `categoria` varchar(50) NOT NULL DEFAULT 'Otros',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla tfg.restaurantes: ~7 rows (aproximadamente)
/*!40000 ALTER TABLE `restaurantes` DISABLE KEYS */;
REPLACE INTO `restaurantes` (`id`, `nombre`, `direccion`, `telefono`, `descripcion`, `categoria`) VALUES
	(1, 'Restaurante 1', 'C/Fake 123', '123456789', '1Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Chino'),
	(2, 'Restaurante 2', 'C/Verdadera 234', '987654321', 'ed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. ', 'Italiano'),
	(3, 'Restaurante 3', 'C/Lorem 1', '456654456', '3Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Otros'),
	(4, 'Restaurante 4', 'C/Lorem 4', '444444444', '4Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Español'),
	(5, 'Restaurante 5', 'C/Ipsum 42', '424242424', '5Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Asiático'),
	(6, 'Hamburgueseria', 'C/Ham 32', '243235436', '6Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Hamburguesería'),
	(7, 'Restaurante 7', 'C/Ipsum 1', '777777777', '7Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Italiano');
/*!40000 ALTER TABLE `restaurantes` ENABLE KEYS */;

-- Volcando estructura para tabla tfg.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `email` varchar(100) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `apellidos` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla tfg.usuarios: ~1 rows (aproximadamente)
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
REPLACE INTO `usuarios` (`email`, `nombre`, `apellidos`) VALUES
	('clemente.varo@gmail.com', 'Alvaro', 'Clemente Verdu'),
	('clementealvaropruebas@gmail.com', 'Alvaro', 'Clemente');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
