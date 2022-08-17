//Yihui Wang 190276
//Juan Jiménez Pérez 190204

package series;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class SeriesDatabase {

	private Connection conn; //Atributo connection

	public SeriesDatabase() {

	}


	//Establecemos la conexión con la base de datos de sql
	public boolean openConnection() {
		boolean abierto=false;
		try {
			if(conn==null || conn.isClosed()) {
				String addr="localhost:3306";
				String db="series";
				String user="series_user";
				String pass="series_pass";
				String url="jdbc:mysql://"+addr+"/"+db;
				conn = DriverManager.getConnection(url, user, pass);
				abierto=true;
			}			
		}catch (SQLException e) {
			System.err.println("Error SQL al abrir la conexión: ");
			System.err.println(e.getMessage());
			abierto=false;
		}catch (Exception e) {
			System.err.println("Otro tipo de error al abrir la conexión: ");
			System.err.println(e.getMessage());
			abierto=false;
		}
		return abierto;
	}


	//Cerramos la conexión con la base de datos de sql
	public boolean closeConnection() {
		boolean cerrado=false;
		if(conn!=null) {
			try {
				conn.close();
				cerrado=true;
			}catch (SQLException e) {
				System.err.println("Error SQL al cerrar la conexión: ");
				System.err.println(e.getMessage());
				cerrado=false;
			}catch (Exception e) {
				System.err.println("Otro tipo de error al cerrar la conexión: ");
				System.err.println(e.getMessage());
				cerrado=false;
			}
		}
		return cerrado;
	}


	//Creamos tabla capitulo
	public boolean createTableCapitulo() {
		boolean creado=false;
		String sql="CREATE TABLE capitulo (" +
				" id_serie INT,"+
				" n_temporada INT,"+
				" n_orden INT," +
				" titulo VARCHAR(100),"+
				" duracion INT," +
				" fecha_estreno DATE,"+
				" PRIMARY KEY (id_serie, n_temporada, n_orden)," +
				" FOREIGN KEY (id_serie) REFERENCES serie (id_serie)"+
				" ON DELETE CASCADE ON UPDATE CASCADE,"+
				" FOREIGN KEY (id_serie, n_temporada) REFERENCES temporada (id_serie, n_temporada)"+
				" ON DELETE CASCADE ON UPDATE CASCADE"+
				");";
		if(conn==null)
			openConnection();		
		Statement st=null;
		try {
			st=conn.createStatement();
			st.executeUpdate(sql);
			creado=true;
		}catch (SQLException e) {
			System.err.println("Error SQL al crear la tabla: ");
			System.err.println(e.getMessage());
			creado=false;
		}catch (Exception e) {
			System.err.println("Otro tipo de error al crear la tabla: ");
			System.err.println(e.getMessage());
			creado=false;
		}finally {
			try {
				if(st!=null)
					st.close();
			}catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());
				creado=false;
			}
		}
		return creado;
	}


	//Creamos tabla valora
	public boolean createTableValora() {
		boolean creado=false;
		String sql="CREATE TABLE valora (" +
				" id_serie INT,"+
				" n_temporada INT,"+
				" n_orden INT," +
				" id_usuario INT,"+
				" fecha DATE,"+
				" valor INT," +
				" PRIMARY KEY (id_serie, n_temporada, n_orden, id_usuario, fecha)," +
				" FOREIGN KEY (id_serie) REFERENCES serie (id_serie)"+
				" ON DELETE CASCADE ON UPDATE CASCADE,"+
				" FOREIGN KEY (id_serie, n_temporada) REFERENCES temporada (id_serie, n_temporada)"+
				" ON DELETE CASCADE ON UPDATE CASCADE,"+
				" FOREIGN KEY (id_serie, n_temporada, n_orden) REFERENCES capitulo (id_serie, n_temporada, n_orden)"+
				" ON DELETE CASCADE ON UPDATE CASCADE,"+
				" FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)"+
				" ON DELETE CASCADE ON UPDATE CASCADE"+
				");";
		if(conn==null)
			openConnection();		
		Statement st=null;
		try {
			st=conn.createStatement();
			st.executeUpdate(sql);
			creado=true;
		}catch (SQLException e) {
			System.err.println("Error SQL al crear la tabla: ");
			System.err.println(e.getMessage());
			creado=false;
		}catch (Exception e) {
			System.err.println("Otro tipo de error al crear la tabla: ");
			System.err.println(e.getMessage());
			creado=false;
		}finally {
			try {
				if(st!=null)
					st.close();
			}catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());
				creado=false;
			}
		}
		return creado;
	}


	//Insertamos los datos el archivo fileName en la tabla capitulo
	public int loadCapitulos(String fileName) {
		String query = "INSERT INTO capitulo (id_serie, n_temporada, n_orden, titulo, duracion, fecha_estreno) VALUES (?,?,?,?,?,?);";
		if(conn==null)
			openConnection();
		Scanner scan=null;	
		ArrayList<String>archivo = new ArrayList<String>();
		ArrayList<capitulo>capitulos = new ArrayList<capitulo>();
		PreparedStatement pst = null;
		int n=0;

		try {
			scan = new Scanner(new FileReader(fileName)).useDelimiter(";|\r\n");
			while(scan.hasNext()) {
				archivo.add(scan.next());
			}
			for (int i=6;i<archivo.size();i=i+6) { 
				capitulos.add(new capitulo(archivo.get(i), archivo.get(i+1), archivo.get(i+2), archivo.get(i+3), archivo.get(i+4), archivo.get(i+5)));
			}
			pst = conn.prepareStatement(query);
			for (int i = 0; i < capitulos.size(); i++) {
				pst.setInt(1, capitulos.get(i).id_serie);
				pst.setInt(2, capitulos.get(i).n_temporada);
				pst.setInt(3, capitulos.get(i).n_orden);
				pst.setString(4, capitulos.get(i).titulo);
				pst.setInt(5, capitulos.get(i).duracion);
				pst.setDate(6, capitulos.get(i).fecha_estreno);		
				if(pst.executeUpdate() == 1)
					n++;
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error al abrir fichero de capitulos:");
			System.err.println(e.getMessage());
		}catch (SQLException e) {
			System.err.println("Error SQL al realizar la insercion de capitulos:");
			System.err.println(e.getMessage());	
		} catch (Exception e) {
			System.err.println("Otro error al realizar la insercion de capitulos:");
			System.err.println(e.getMessage());	
		} finally{
			try {
				if(scan!=null) scan.close();
				if(pst!=null) pst.close();
			} catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());	
			} 
		}
		return n;
	}


	//Insertamos los datos el archivo fileName en la tabla valora
	public int loadValoraciones(String fileName) {
		String query = "INSERT INTO valora (id_serie, n_temporada, n_orden, id_usuario, fecha, valor) VALUES (?,?,?,?,?,?);";
		if(conn==null)
			openConnection();
		Scanner scan=null;	
		ArrayList<String>archivo = new ArrayList<String>();
		ArrayList<valora>valoraciones = new ArrayList<valora>();
		PreparedStatement pst = null;
		int n=0;

		try {
			scan = new Scanner(new FileReader(fileName)).useDelimiter(";|\r\n");
			while(scan.hasNext()) {
				archivo.add(scan.next());
			}
			for (int i=6;i<archivo.size();i=i+6) { 
				valoraciones.add(new valora(archivo.get(i), archivo.get(i+1), archivo.get(i+2), archivo.get(i+3), archivo.get(i+4), archivo.get(i+5)));
			}
			pst = conn.prepareStatement(query);
			for (int i = 0; i < valoraciones.size(); i++) {
				pst.setInt(1, valoraciones.get(i).id_serie);
				pst.setInt(2, valoraciones.get(i).n_temporada);
				pst.setInt(3, valoraciones.get(i).n_orden);
				pst.setInt(4, valoraciones.get(i).id_usuario);
				pst.setDate(5, valoraciones.get(i).fecha);
				pst.setInt(6, valoraciones.get(i).valor);
				int exUpdate = pst.executeUpdate();			
				if(exUpdate == 1)
					n++;
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error al abrir fichero de valoraciones:");
			System.err.println(e.getMessage());
		}catch (SQLException e) {
			System.err.println("Error SQL al realizar la insercion de valoraciones:");
			System.err.println(e.getMessage());	
		} catch (Exception e) {
			System.err.println("Otro error al realizar la insercion de valoraciones:");
			System.err.println(e.getMessage());	
		} finally{
			try {
				if(scan!=null) scan.close();
				if(pst!=null) pst.close();
			} catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());	
			} 
		}
		return n;
	}


	//Consultamos la lista de series y temporadas para imprimir el nombre de las series con sus respectivos capitulos ordenados de manera ascendente
	public String catalogo() {
		String sql="SELECT s.titulo, t.n_capitulos FROM serie s\r\n"
				+ "INNER JOIN temporada t ON s.id_serie=t.id_serie ORDER BY s.id_serie, t.n_capitulos ASC;";
		if(conn==null)
			openConnection();		
		Statement st=null;
		ResultSet rs=null;
		String cadena=null;
		String titulo=null;
		try {
			st = conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()) {
				cadena="{"+rs.getString("titulo")+":[";
				if(rs.getInt("n_capitulos")!=0)
					cadena=cadena+rs.getInt("n_capitulos");
				titulo=rs.getString("titulo");
				while(rs.next()){
					if(!titulo.equals(rs.getString("titulo"))) {
						cadena=cadena+"],"+rs.getString("titulo")+":[";
						if(rs.getInt("n_capitulos")!=0)
							cadena=cadena+rs.getInt("n_capitulos");
						titulo=rs.getString("titulo");
					}
					else {
						cadena=cadena+","+ rs.getInt("n_capitulos");
					}
				}
				cadena=cadena+"]}";
			}
			else
				cadena="{}";
		} catch (SQLException e) {
			System.err.println("Error SQL al ejecutar la consulta de catalogo: ");
			System.err.println(e.getMessage());
			cadena=null;
		} catch (Exception e) {
			System.err.println("Otro tipo de error al ejecutar la consulta de catalogo: ");
			System.err.println(e.getMessage());
			cadena=null;
		}finally {
			try {
				if(st!=null) 
					st.close();
				if(st!=null)
					rs.close();
			}catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());
				cadena=null;
			}
		}
		return cadena;
	}


	//Consultamos las tablas usuario y comenta y devolvemos el listado de usuarios que no hayan comentado ninguna serie
	public String noHanComentado() {
		String sql= "SELECT u.nombre, u.apellido1, u.apellido2 FROM usuario u\r\n"
				+ "LEFT JOIN comenta c ON u.id_usuario=c.id_usuario WHERE texto IS NULL ORDER BY u.apellido1, u.apellido2, u.nombre ASC;";
		if(conn==null)
			openConnection();		
		Statement st=null;
		ResultSet rs=null;
		String cadena=null;
		String titulo=null;
		try {
			st = conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()){
				cadena="["+rs.getString("nombre")+" "+rs.getString("apellido1")+" "+rs.getString("apellido2");
				while(rs.next()){
					cadena=cadena+", "+rs.getString("nombre")+" "+rs.getString("apellido1")+" "+rs.getString("apellido2");
				}
				cadena=cadena+"]";
			}
			else {
				cadena="[]";
			}
		} catch (SQLException e) {
			System.err.println("Error SQL al ejecutar la consulta de los usuarios que no han comentado: ");
			System.err.println(e.getMessage());
			cadena=null;
		} catch (Exception e) {
			System.err.println("Otro tipo de error al ejecutar la consulta de los usuarios que no han comentado: ");
			System.err.println(e.getMessage());
			cadena=null;
		}finally {
			try {
				if(st!=null) 
					st.close();
				if(st!=null)
					rs.close();
			}catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());
				cadena=null;
			}
		}
		return cadena;
	}


	//Calculamos la media de la valoración de la series cuyo genero coincide con el que nos pasan como parametro
	public double mediaGenero(String genero) {
		String sql="SELECT AVG(v.valor) FROM \r\n"
				+ "(SELECT p.id_serie, g.descripcion FROM genero g INNER JOIN pertenece p ON g.id_genero=p.id_genero WHERE g.descripcion=?) AS tabla\r\n"
				+ "INNER JOIN valora v ON v.id_serie=tabla.id_serie;";
		if(conn==null)
			openConnection();
		PreparedStatement pst = null;
		ResultSet rs=null;
		Double resultado=0.0;
		try {
			pst=conn.prepareStatement(sql);
			pst.setString(1, genero);
			rs=pst.executeQuery();
			rs.next();
			if(rs.getDouble(1)!=0)
				resultado=rs.getDouble(1);
			else
				resultado=-1.0;
		}catch (SQLException e) {
			System.err.println("Error SQL al calcular la media por genero:");
			System.err.println(e.getMessage());	
			resultado=-2.0;
		} catch (Exception e) {
			System.err.println("Otro error al calcular la media por genero:");
			System.err.println(e.getMessage());	
			resultado=-2.0;
		} finally{
			try {
				if(pst!=null) pst.close();
				if(rs!=null) rs.close();
			} catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());	
				resultado=-2.0;
			} 
		}
		return resultado;
	}


	//Calculamos el tiempo medio de duracion de los capitulos que estan en el idioma que nos pasan como parametro y que no hayan recibido comentarios
	public double duracionMedia(String idioma) {
		String sql="SELECT AVG(tabla.duracion) FROM \r\n"
				+ "(SELECT s.id_serie, c.duracion FROM serie s INNER JOIN capitulo c ON s.id_serie=c.id_serie WHERE s.idioma=?) AS tabla\r\n"
				+ "LEFT JOIN comenta c ON c.id_serie=tabla.id_serie WHERE c.texto IS NULL;";
		if(conn==null)
			openConnection();
		PreparedStatement pst = null;
		ResultSet rs=null;
		Double resultado=0.0;
		try {
			pst=conn.prepareStatement(sql);
			pst.setString(1, idioma);
			rs=pst.executeQuery();
			rs.next();
			if(rs.getDouble(1)!=0)
				resultado=rs.getDouble(1);
			else
				resultado=-1.0;
		}catch (SQLException e) {
			System.err.println("Error SQL al calcular la media por genero:");
			System.err.println(e.getMessage());	
			resultado=-2.0;
		} catch (Exception e) {
			System.err.println("Otro error al calcular la media por genero:");
			System.err.println(e.getMessage());	
			resultado=-2.0;
		} finally{
			try {
				if(pst!=null) pst.close();
				if(rs!=null) rs.close();
			} catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());	
				resultado=-2.0;
			} 
		}
		return resultado;
	}


	//Asignamos una foto de usuario al usuario que tenga como apellido cabeza
	public boolean setFoto(String filename) {
		if (conn == null) {
			openConnection();
		}
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean insertaFoto = true; // insertaFoto = true; la foto se va a insertar, en caso contrario se saltara error								
		FileInputStream ficheroSt = null;		
		int cont = 0; // contador de usuarios de apellido 'Cabeza'; si cont > 1 saltara error
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT apellido1,fotografia FROM usuario" + " WHERE apellido1 = \"Cabeza\";");
			while (rs.next()) {
				if (rs.getBlob("fotografia") != null) // Se comprueba si el usuario ya tiene la foto
					insertaFoto = false; // En caso positivo, se saltara un error
				cont++;
			}
			if (cont > 1 || !insertaFoto) {
				System.err.println("Error: Existe mas de un usuario cuyo primer apellido “Cabeza” / El usuario ya tiene una foto");
				return insertaFoto;
			} else if (cont == 1 && insertaFoto) {
				try {
					pst = conn.prepareStatement("UPDATE usuario SET fotografia = ? " + "WHERE apellido1 = \"Cabeza\"");
					File ptrFile = new File(filename); // puntero al fichero
					ficheroSt = new FileInputStream(ptrFile);
					pst.setBinaryStream(1, ficheroSt, ptrFile.length()); 
					pst.executeUpdate();
					System.out.println("Se ha añadido correctamente la foto:");
				} catch (FileNotFoundException e) {
					System.err.println("Error SQL al añadir una foto.");
					System.err.println(e.getMessage());
					return false;
				} catch (SQLException e) {
					System.err.println("Error: El archivo de la foto no se ha encontrado.");
					System.err.println(e.getMessage() + e.getStackTrace());
					return false;
				}
			} else {
				System.out.println("Error:No existe usuarios con apellido 'Cabeza'.");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Error: El archivo de la foto no se ha encontrado.");
			System.err.println(e.getMessage());
		} finally {
			try {
				if (st != null) st.close();
				if (pst != null) pst.close();
				if (ficheroSt != null) ficheroSt.close();
				if (rs != null) rs.close();
			} catch (SQLException e) {
				System.err.println("Error al cerrar las estructuras.");
				System.err.println(e.getMessage());
			} catch (IOException e) {
				System.err.println("Error al cerrar el fichero.");
				System.err.println(e.getMessage());
			}
		}
		return insertaFoto;
	}


	//Clase inner para insertar datos a la tabla capitulo
	private class capitulo{	
		public int id_serie;
		public int n_temporada;
		public int n_orden;
		public Date fecha_estreno;
		public String titulo;
		public int duracion;

		public capitulo(String id_serie, String n_temporada, String n_orden, String fecha_estreno, String titulo, String duracion) {
			this.id_serie = Integer.parseInt(id_serie);
			this.n_temporada = Integer.parseInt(n_temporada);
			this.n_orden = Integer.parseInt(n_orden);
			String[] fecha = fecha_estreno.split("-");
			this.fecha_estreno = new Date(Integer.parseInt(fecha[2]),Integer.parseInt(fecha[1]),Integer.parseInt(fecha[0]));
			this.titulo = titulo;
			this.duracion = Integer.parseInt(duracion);
		}
	}


	//Clase inner para insertar datos a la tabla valora
	private class valora{
		public int id_serie;
		public int n_temporada;
		public int n_orden;
		public int id_usuario;
		public int valor;
		public Date fecha;


		public valora(String id_serie, String n_temporada, String n_orden, String id_usuario, String fecha, String valor) {
			this.id_serie = Integer.parseInt(id_serie);
			this.n_temporada = Integer.parseInt(n_temporada);
			this.n_orden = Integer.parseInt(n_orden);
			this.id_usuario = Integer.parseInt(id_usuario);
			this.valor = Integer.parseInt(valor);
			String[] date = fecha.split("-");
			this.fecha = new Date(Integer.parseInt(date[2]),Integer.parseInt(date[1]),Integer.parseInt(date[0]));
		}
	}

}
