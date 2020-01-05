-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-01-2020 a las 15:57:54
-- Versión del servidor: 10.4.6-MariaDB
-- Versión de PHP: 7.1.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `people`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contacto`
--

CREATE TABLE `contacto` (
  `idContacto` varchar(255) NOT NULL,
  `primerNombre` varchar(40) NOT NULL,
  `primerApellido` varchar(40) DEFAULT NULL,
  `telefono` varchar(10) DEFAULT NULL,
  `correo` varchar(254) DEFAULT NULL,
  `idUsuario` int(11) NOT NULL,
  `version` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `contacto`
--

INSERT INTO `contacto` (`idContacto`, `primerNombre`, `primerApellido`, `telefono`, `correo`, `idUsuario`, `version`) VALUES
('C-0a6896ba-d013-4aac-a504-6d06d6407754', 'Ana Cambio ', 'Estivas', '0000000', 'ana_es@mail.com', 2, '2015-12-28 16:55:29'),
('C-1f3441e3-78ce-4fe4-9d5c-08606ca195f6', 'pepe', 'ramirez ', '25', 'ncrcollazos@gmail.com', 2, '2019-12-28 22:51:14'),
('C-5b44a68c-0b2c-4e30-bf9c-580763e13e59', 'Manuel ', 'Lopez ', '3332', 'ffff', 2, '2019-12-28 22:53:29'),
('C-5f948e45-35a4-49fb-9001-54608959311c', 'Patricia ', 'Fernández ', '3288', 'ncrcollazos@gmail.com', 2, '2019-12-28 06:40:57'),
('C-785bd1c3-5627-47c5-ada8-8ba869bae1d9', 'Norbey ', 'Collazos ', '123', 'ncrcollazos@gmail.com', 2, '2019-12-27 15:07:57'),
('C-942eac87-af76-4f13-8680-47a42ee46c4f', 'Roberto', 'Gomez', '4444444', 'robertico@mail.com', 2, '2019-12-27 15:07:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `contrasena` varchar(128) NOT NULL,
  `claveApi` varchar(60) NOT NULL,
  `correo` varchar(254) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `nombre`, `contrasena`, `claveApi`, `correo`) VALUES
(1, 'jamesr', '$2y$10$RTDLiEESF.j8bRZJwHBY4.QvU4hie5bKXF7ZvIvuR6YPdNIWKOAYO', '51f12924bb948d688cc9e79abf0abc7a', 'james@mail.com'),
(2, 'maestro', '$2y$10$8Xn6TT203fO9PWyxyXaL2e4BGmo40sJKyOU4Sz9H4eSZFKoulcyg.', '60d5b4e60cb6a70898f0cd17174e9edd', 'master@mail.com');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `contacto`
--
ALTER TABLE `contacto`
  ADD PRIMARY KEY (`idContacto`),
  ADD KEY `idUsuario` (`idUsuario`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `contacto`
--
ALTER TABLE `contacto`
  ADD CONSTRAINT `contacto_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
