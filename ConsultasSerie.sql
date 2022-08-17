#Yihui Wang 190276
#Juan Jiménez Pérez 190204

USE series;
CREATE TABLE capitulo (
id_serie INT,
n_temporada INT,
n_orden INT,
titulo VARCHAR(100),
duracion INT,
fecha_estreno DATE,
PRIMARY KEY (id_serie, n_temporada, n_orden),
FOREIGN KEY (id_serie) REFERENCES serie (id_serie)
	ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (id_serie, n_temporada) REFERENCES temporada (id_serie, n_temporada)
	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE valora (
id_serie INT,
n_temporada INT,
n_orden INT,
id_usuario INT,
fecha DATE,
valor INT,
PRIMARY KEY (id_serie, n_temporada, n_orden, id_usuario, fecha),
FOREIGN KEY (id_serie) REFERENCES serie (id_serie)
	ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (id_serie, n_temporada) REFERENCES temporada (id_serie, n_temporada)
	ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (id_serie, n_temporada, n_orden) REFERENCES capitulo (id_serie, n_temporada, n_orden)
	ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
	ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO capitulo (id_serie, n_temporada, n_orden, titulo, duracion, fecha_estreno) VALUES (1,1,1,"Pilot",30,24/09/2007);

INSERT INTO valora (id_serie, n_temporada, n_orden, id_usuario, fecha, valor) VALUES (1,1,1,4,16/06/2020,8);

SELECT s.titulo, t.n_capitulos FROM serie s
INNER JOIN temporada t ON s.id_serie=t.id_serie ORDER BY s.id_serie, t.n_capitulos ASC;

SELECT u.nombre, u.apellido1, u.apellido2 FROM usuario u
LEFT JOIN comenta c ON u.id_usuario=c.id_usuario WHERE texto IS NULL ORDER BY u.apellido1, u.apellido2, u.nombre ASC;

SELECT AVG(v.valor) FROM 
(SELECT p.id_serie, g.descripcion FROM genero g INNER JOIN pertenece p ON g.id_genero=p.id_genero WHERE g.descripcion="Comedia") AS tabla 
INNER JOIN valora v ON v.id_serie=tabla.id_serie;

SELECT AVG(tabla.duracion) FROM 
(SELECT s.id_serie, c.duracion FROM serie s INNER JOIN capitulo c ON s.id_serie=c.id_serie WHERE s.idioma="Ingles") AS tabla
LEFT JOIN comenta c ON c.id_serie=tabla.id_serie WHERE c.texto IS NULL;

SELECT apellido1, fotografia FROM usuario WHERE apellido1 = "Cabeza";

UPDATE usuario SET fotografia = "HommerSimpson.jpg" WHERE apellido1 ="Cabeza";SELECT AVG(v.valor) FROM  (SELECT p.id_serie, g.descripcion FROM genero g INNER JOIN pertenece p ON g.id_genero=p.id_genero WHERE g.descripcion=?) AS tabla INNER JOIN valora v ON v.id_serie=tabla.id_serie;
