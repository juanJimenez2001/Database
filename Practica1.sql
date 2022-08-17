CREATE DATABASE Practica1;
USE Practica1;

CREATE TABLE Clasificaciones_de_PEGI (
identificador INT UNIQUE NOT NULL, 
edad_minima INT,
descripcion VARCHAR(50),
PRIMARY KEY(identificador)
);

CREATE TABLE Genero(
ident INT UNIQUE NOT NULL, 
descripcion VARCHAR(50), 
PRIMARY KEY(ident)
 );
 
CREATE TABLE Rol(
identificador INT UNIQUE NOT NULL, 
descripcion VARCHAR(50), 
PRIMARY KEY(identificador)
 );

CREATE TABLE Serie (
ident INT UNIQUE NOT NULL, 
titulo VARCHAR(50), 
idioma VARCHAR(50), 
fecha_estreno date, 
sinopsis VARCHAR(200),
identificador_clasificacion_pegi INT NOT NULL,
PRIMARY KEY(ident),
FOREIGN KEY(identificador_clasificacion_pegi) REFERENCES Clasificaciones_de_PEGI(identificador)
ON DELETE CASCADE ON UPDATE CASCADE
);
 
CREATE TABLE Temporadas (
ident INT UNIQUE NOT NULL, 
Titulo VARCHAR(50), 
fecha_estreno DATE, 
sinopsis VARCHAR(200), 
numero_de_temporada INT UNIQUE NOT NULL, 
numero_de_capitulos INT,
PRIMARY KEY(ident, numero_de_temporada),
FOREIGN KEY(ident) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Capitulo (
ident INT UNIQUE NOT NULL,
numero_de_orden INT UNIQUE NOT NULL,
titulo VARCHAR(50),
sinopsis VARCHAR(50), 
duracion INT,
fecha_estreno DATE, 
numero_de_temporada INT UNIQUE NOT NULL,
PRIMARY KEY(ident, numero_de_temporada, numero_de_orden),
FOREIGN KEY(ident) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_temporada) REFERENCES Temporadas(numero_de_temporada)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Escena (
ident INT UNIQUE NOT NULL, 
minuto_comienzo INT,
duracion INT,
numero_de_temporada INT UNIQUE NOT NULL,
ident_serie INT UNIQUE NOT NULL,
numero_de_orden INT UNIQUE NOT NULL,
PRIMARY KEY(ident, numero_de_temporada, ident_serie, numero_de_orden),
FOREIGN KEY(ident_serie) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_temporada) REFERENCES Temporadas(numero_de_temporada)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_orden) REFERENCES Capitulo(numero_de_orden)
ON DELETE CASCADE ON UPDATE CASCADE
);
 
CREATE TABLE Personas(
ident INT UNIQUE NOT NULL, 
nombre VARCHAR(50), 
apellidos_apellido1 VARCHAR(50), 
apellidos_apellido2 VARCHAR(50), 
nombre_artistico VARCHAR(50), 
fecha_nacimiento DATE,
ciudad_nacimiento VARCHAR(50), 
nacionalidad VARCHAR(50), 
PRIMARY KEY(ident)
 );
 
CREATE TABLE Plataformas(
url VARCHAR(50) UNIQUE NOT NULL, 
nombre_comercial VARCHAR(50), 
PRIMARY KEY(url)
 );
 
CREATE TABLE Usuario(
correo_electronico VARCHAR(50) UNIQUE NOT NULL, 
tratamiento VARCHAR(50), 
nombre VARCHAR(50),
apellidos_apellido1 VARCHAR(50), 
apellidos_apellido2 VARCHAR(50), 
contraseña VARCHAR(50)  NOT NULL, 
fecha_de_alta DATE NOT NULL, 
correo_electronico_registro VARCHAR(50) UNIQUE NOT NULL, 
PRIMARY KEY(correo_electronico)
 );
 
CREATE TABLE Valoraciones(
ident INT UNIQUE NOT NULL, 
fecha DATE,
comentario VARCHAR(50),
nota INT NOT NULL,
PRIMARY KEY(ident)
 );
 
CREATE TABLE Tipo_de_comentario(
ident INT UNIQUE NOT NULL, 
descripcion VARCHAR(50),
PRIMARY KEY(ident)
 );
 
CREATE TABLE Comentario(
ident INT UNIQUE NOT NULL, 
fecha_de_cracion DATE,
comentario VARCHAR(50),
ident_tipo_de_comentario INT UNIQUE NOT NULL,
PRIMARY KEY(ident),
FOREIGN KEY(ident_tipo_de_comentario) REFERENCES Tipo_de_comentario(ident)
ON DELETE CASCADE ON UPDATE CASCADE
 );
 
CREATE TABLE Disponible(
fechas_de_inicio DATE UNIQUE NOT NULL, 
fechas_de_fin DATE,
url_plataformas VARCHAR(50) UNIQUE NOT NULL,
ident_serie INT UNIQUE NOT NULL,
numero_de_temporada INT UNIQUE NOT NULL,
PRIMARY KEY(fechas_de_inicio, url_plataformas, ident_serie, numero_de_temporada),
FOREIGN KEY(ident_serie) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(url_plataformas) REFERENCES Plataformas(url)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_temporada) REFERENCES Temporadas(numero_de_temporada)
ON DELETE CASCADE ON UPDATE CASCADE
 );
 
CREATE TABLE Pertenece (
ident_genero INT UNIQUE NOT NULL, 
ident_serie INT UNIQUE NOT NULL,
PRIMARY KEY(ident_genero, ident_serie),
FOREIGN KEY(ident_serie) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(ident_genero) REFERENCES Genero(ident)
ON DELETE CASCADE ON UPDATE CASCADE
 );
 
CREATE TABLE Solicitud_de_amistad (
correo_electronico_usuario_solicitante VARCHAR(50) UNIQUE NOT NULL, 
fecha_creacion DATE UNIQUE NOT NULL,
fecha_contestacion DATE UNIQUE NOT NULL,
estado_solicitud BOOLEAN,
correo_electronico_usuario_solicitado VARCHAR(50) UNIQUE NOT NULL,
PRIMARY KEY(correo_electronico_usuario_solicitante,fecha_creacion,correo_electronico_usuario_solicitado),
FOREIGN KEY(correo_electronico_usuario_solicitante) REFERENCES Usuario(correo_electronico)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(correo_electronico_usuario_solicitado) REFERENCES Usuario(correo_electronico)
ON DELETE CASCADE ON UPDATE CASCADE
 );
 
CREATE TABLE Desempeña (
ident_serie INT UNIQUE NOT NULL,
ident_persona INT UNIQUE NOT NULL,
identificador_rol INT UNIQUE NOT NULL,
PRIMARY KEY(ident_serie, ident_persona, identificador_rol),
FOREIGN KEY(ident_serie) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(ident_persona) REFERENCES Personas(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(identificador_rol) REFERENCES Rol(identificador)
ON DELETE CASCADE ON UPDATE CASCADE
 );
 
CREATE TABLE Participa (
ident_serie INT UNIQUE NOT NULL,
ident_persona INT UNIQUE NOT NULL,
identificador_rol INT UNIQUE NOT NULL,
numero_de_orden_capitulos INT UNIQUE NOT NULL,
numero_de_temporada INT UNIQUE NOT NULL,
PRIMARY KEY(ident_serie, ident_persona, identificador_rol, numero_de_orden_capitulos, numero_de_temporada),
FOREIGN KEY(ident_serie) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(ident_persona) REFERENCES Personas(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(identificador_rol) REFERENCES Rol(identificador)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_orden_capitulos) REFERENCES Capitulo(numero_de_orden)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_temporada) REFERENCES Temporadas(numero_de_temporada)
ON DELETE CASCADE ON UPDATE CASCADE
 );
 
CREATE TABLE Comentario_escena (
correo_electronico_usuario VARCHAR(50) UNIQUE NOT NULL,
ident_comentario INT UNIQUE NOT NULL,
ident_escena INT UNIQUE NOT NULL,
numero_de_temporada_Temporadas INT UNIQUE NOT NULL,
numero_de_orden_capitulo INT UNIQUE NOT NULL,
ident_serie INT UNIQUE NOT NULL,
PRIMARY KEY(correo_electronico_usuario, ident_comentario, ident_escena, numero_de_temporada_Temporadas, numero_de_orden_capitulo, ident_serie),
FOREIGN KEY(correo_electronico_usuario) REFERENCES Usuario(correo_electronico)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(ident_comentario) REFERENCES Comentario(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(ident_escena) REFERENCES Escena(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_temporada_Temporadas) REFERENCES Temporadas(numero_de_temporada)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_orden_capitulo) REFERENCES Capitulo(numero_de_orden)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(ident_serie) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE
 );
 
CREATE TABLE Valora (
correo_electronico_usuario VARCHAR(50) UNIQUE NOT NULL,
ident_valoracion INT UNIQUE NOT NULL,
ident_serie INT UNIQUE NOT NULL,
numero_de_orden_capitulos INT UNIQUE NOT NULL,
numero_de_temporada_temporadas INT UNIQUE NOT NULL,
PRIMARY KEY(correo_electronico_usuario, ident_valoracion, ident_serie),
FOREIGN KEY(correo_electronico_usuario) REFERENCES Usuario(correo_electronico)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(ident_valoracion) REFERENCES Valoraciones(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(ident_serie) REFERENCES Serie(ident)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_orden_capitulos) REFERENCES Capitulo(numero_de_orden)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(numero_de_temporada_temporadas) REFERENCES Temporadas(numero_de_temporada)
ON DELETE CASCADE ON UPDATE CASCADE
 );
 