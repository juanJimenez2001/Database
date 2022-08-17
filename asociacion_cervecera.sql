#Definición del esquema y las tablas

CREATE SCHEMA asociacion_cervecera;
USE asociacion_cervecera;

CREATE TABLE socio (
  ID_socio INT UNIQUE NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  direccion VARCHAR(100),
  telefono VARCHAR(15),
  PRIMARY KEY (ID_socio)
);

CREATE TABLE bar (
  licencia CHAR(5) UNIQUE NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  dirección VARCHAR(100),
  PRIMARY KEY (licencia)
);

CREATE TABLE fabricante (
  ID_fabricante INT NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  telefono VARCHAR(15) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE,
  PRIMARY KEY (ID_fabricante)
);

CREATE TABLE cerveza (
  ID_cerveza INT NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  caracteristicas VARCHAR(100),
  ID_fabricante INT,
  PRIMARY KEY (ID_cerveza),
  FOREIGN KEY (ID_fabricante) REFERENCES fabricante (ID_fabricante)
      ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE gusta (
  ID_socio INT NOT NULL,
  ID_cerveza INT NOT NULL,
  PRIMARY KEY (ID_socio, ID_cerveza),
  FOREIGN KEY (ID_socio) REFERENCES socio (ID_socio)
      ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (ID_cerveza) REFERENCES cerveza (ID_cerveza)
      ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE vende (
licencia CHAR(5) NOT NULL,
ID_cerveza INT NOT NULL,
precio FLOAT,
PRIMARY KEY (licencia, ID_cerveza),
FOREIGN KEY (licencia) REFERENCES bar (licencia)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (ID_cerveza) REFERENCES cerveza (ID_cerveza)
ON DELETE CASCADE ON UPDATE CASCADE
);



# Inserción de datos

INSERT INTO fabricante VALUES (1, 'Mahou', '910112233', 'mahou@mahou.es');
INSERT INTO fabricante VALUES (2, 'Cruzcampo', '950332211', 'cp@cruzcampo.es');
INSERT INTO fabricante VALUES (3, 'Boostels', '+3252001122', 'boostels@boostels.be');
INSERT INTO fabricante VALUES (4, 'Duvel', '+3232221100', 'duvel@duvel.be');


INSERT INTO cerveza VALUES (1, 'Cinco Estrellas', 'Rubia', 1);
INSERT INTO cerveza VALUES (2, 'Maestra', 'Tostada', 1);
INSERT INTO cerveza VALUES (3, 'Mahou Sin', 'Sin alcohol', 1);
INSERT INTO cerveza VALUES (4, 'Especial', 'Otra rubia', 2);
INSERT INTO cerveza VALUES (5, 'Radler', 'Con limón', 2);
INSERT INTO cerveza VALUES (6, 'Cero cero', 'Sin alcohol', 2);
INSERT INTO cerveza VALUES (7, 'Tripel Karmeliet', 'Para masticar, pero rica', 3);
INSERT INTO cerveza VALUES (8, 'Kwak', 'Típica belga, algo fuerte', 3);
INSERT INTO cerveza VALUES (9, 'DeuS', '', 3);
INSERT INTO cerveza VALUES (10, 'Duvel', 'Nada más belga que Duvel', 4);
INSERT INTO cerveza VALUES (11, 'La Chouffe', '', 4);
INSERT INTO cerveza VALUES (12, 'Barrica de Mahou', 'Envejecida en barrica de roble', 1);


INSERT INTO bar VALUES ('ES-a1', 'La Pirata de Malasaña', 'C/ Manuela Malasaña, 20, Madrid');
INSERT INTO bar VALUES ('ES-b2', 'Taproom Madrid', 'C/ Guzmán el Bueno, 52, Madrid');
INSERT INTO bar VALUES ('BE-c3', 'Dulle Griet', 'Vrijdagmarkt 50, Gante');
INSERT INTO bar VALUES ('BE-d4', 'Delirium Café', 'Impasse de la Fidélité 4, Bruselas');


INSERT INTO socio VALUES (1, 'Lucía García', 'C/ Norte, 1', '600000099');
INSERT INTO socio(ID_socio, nombre, direccion) VALUES (2, 'Miguel Aragón', 'C/ Sur, 2');
INSERT INTO socio(ID_socio, telefono, nombre) VALUES (3, '600000077', 'Luisa Pérez');
INSERT INTO socio VALUES (4, 'Ángel Collado', 'C/ Oeste, 4', '+34600000066');


INSERT INTO gusta(ID_socio, ID_cerveza) VALUES (1, 1);
INSERT INTO gusta VALUES (1, 3);
INSERT INTO gusta VALUES (1, 4);
INSERT INTO gusta VALUES (1, 5);
INSERT INTO gusta VALUES (1, 6);

INSERT INTO gusta VALUES (2, 1);
INSERT INTO gusta VALUES (2, 2);
INSERT INTO gusta VALUES (2, 4);
INSERT INTO gusta VALUES (2, 9);
INSERT INTO gusta VALUES (2, 11);

INSERT INTO gusta VALUES (3, 2);
INSERT INTO gusta VALUES (3, 3);
INSERT INTO gusta VALUES (3, 7);
INSERT INTO gusta VALUES (3, 8);
INSERT INTO gusta VALUES (3, 9);
INSERT INTO gusta VALUES (3, 10);

INSERT INTO gusta VALUES (4, 1);
INSERT INTO gusta VALUES (4, 2);
INSERT INTO gusta VALUES (4, 4);
INSERT INTO gusta VALUES (4, 5);
INSERT INTO gusta VALUES (4, 7);
INSERT INTO gusta VALUES (4, 8);
INSERT INTO gusta VALUES (4, 10);
INSERT INTO gusta VALUES (4, 11);

INSERT INTO vende VALUES ("ES-a1", 1, 2.5);
INSERT INTO vende VALUES ("ES-a1", 2, 3);
INSERT INTO vende VALUES ("ES-a1", 3, 2.5);
INSERT INTO vende VALUES ("ES-a1", 10, 3.5);
INSERT INTO vende VALUES ("ES-a1", 11, 4.5);
INSERT INTO vende VALUES ("ES-b2", 4, 2);
INSERT INTO vende VALUES ("ES-b2", 5, 2);
INSERT INTO vende VALUES ("ES-b2", 6, 2);
INSERT INTO vende VALUES ("ES-b2", 1, NULL);
INSERT INTO vende VALUES ("ES-b2", 3, NULL);
INSERT INTO vende VALUES ("BE-c3", 7, 5);
INSERT INTO vende VALUES ("BE-c3", 8, 5.5);
INSERT INTO vende VALUES ("BE-c3", 9, 5);
INSERT INTO vende VALUES ("BE-c3", 10, 4.5);
INSERT INTO vende VALUES ("BE-d4", 2, 4);
INSERT INTO vende VALUES ("BE-d4", 7, 6);
INSERT INTO vende VALUES ("BE-d4", 8, NULL);
INSERT INTO vende VALUES ("BE-d4", 9, 5.5);
INSERT INTO vende VALUES ("BE-d4", 10, 4);
INSERT INTO vende VALUES ("BE-d4", 11, 5);

SELECT * FROM cerveza;

ALTER TABLE socio ADD COLUMN foto MEDIUMBLOB;