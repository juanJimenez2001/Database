package Main;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class Ejemplo1 {

	private Connection conn;

	public void connect(){
		try {
			if(conn==null || conn.isClosed()) {
				String addr="127.0.0.1:3306";
				String db="asociacion_cervecera";
				String user="user";
				String pass="userpass";
				String url="jdbc:mysql://"+addr+"/"+db;

				conn = DriverManager.getConnection(url, user, pass);
			}			
		}catch (SQLException e) {
			System.err.println("Error al abrir la conexión: ");
			System.err.println(e.getMessage());
		}
	}

	public void disconnect() throws SQLException {
		if(conn!=null)
			conn.close();
	}

	public void queryCervezas(){
		String sql="SELECT * FROM cerveza;";
		connect();
		Statement st=null;
		ResultSet rs=null;
		try {
			st = conn.createStatement();
			rs=st.executeQuery(sql);
			System.out.println("Consulta ejecutada correctamente");
			while(rs.next()) {
				System.out.println(" - "+ rs.getString("nombre"));
			}
		} catch (SQLException e) {
			System.err.println("Error SQL al ejecutar la consulta de las cervezas: ");
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Otro tipo de error al ejecutar la consulta de las cervezas: ");
			System.err.println(e.getMessage());
		}finally {
			try {
				if(st!=null) 
					st.close();
				if(st!=null)
					rs.close();
			}catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());
			}
		}
	}

	public void createTableEmpleado() throws SQLException {
		String sql="CREATE TABLE empleado (" +
				" ID_empleado INT," +
				" nombre VARCHAR(100)," +
				" PRIMARY KEY(ID_EMPLEADO)" +
				");";
		connect();
		Statement st=conn.createStatement();
		st.executeUpdate(sql);
		st.close();
	}

	public void updateNombreSocios() throws SQLException {
		String sql="UPDATE socio " +
				"SET nombre = 'Hola Mundo' " +
				"WHERE nombre LIKE 'L%';";
		connect();
		Statement st=conn.createStatement();
		int n=st.executeUpdate(sql);
		System.out.println("Socios actualizados: "+n);
		st.close();
	}

	public void getSOcio(int id) {
		String sql= "SELECT * FROM socio WHERE ID_socio=?;";
		this.connect();
		PreparedStatement pst=null;	
		ResultSet rs=null;
		try {
			pst=conn.prepareStatement(sql);
			pst.setInt(1, id);
			rs=pst.executeQuery();
			if(rs.next())
				System.out.println("Nombre socio: "+rs.getString("nombre"));
			else
				System.out.println("No existe ningun socio con ese identificador");
		}catch (SQLException e) {
			System.err.println("Error SQL al consultar socio por ID: ");
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Otro tipo de error al al consultar socio por ID: ");
			System.err.println(e.getMessage());
		}finally {
			try {
				if(pst!=null) 
					pst.close();
				if(rs!=null)
					rs.close();
			}catch (SQLException e) {
				System.err.println("Error al liberar recursos: ");
				System.err.println(e.getMessage());
			}
		}
	}

	public void getDBMetaData() {
		connect();
		try {
			DatabaseMetaData dbm =conn.getMetaData();
			System.out.println("Gestor: "+dbm.getDatabaseProductName());
			System.out.println("Version: "+dbm.getDatabaseProductVersion());
		} catch (SQLException e) {
			System.err.println("Error SQL al obtener los metadatos: ");
			System.err.println(e.getMessage());
		}
	}

	public void getTableMetadata() {
		PreparedStatement pst=null;
		ResultSet rs=null;
		this.connect();
		try {
			pst=conn.prepareStatement("SELECT *FROM cerveza;");
			rs=pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println("Numero columnas: "+rsmd.getColumnCount());
			for(int i=1; i<rsmd.getColumnCount(); i++) {
				System.out.println("Nombre columna: "+rsmd.getColumnName(i));
				System.out.println("Etiqueta columna: "+rsmd.getColumnLabel(i));
				System.out.println("Tipo columna: "+rsmd.getColumnTypeName(i)+"\n");
			}
		}catch(SQLException e) {
			System.err.println("Error SQL al obtener los metadatos de la tabla: ");
			System.err.println(e.getMessage());
		}finally {
			try {
				if(pst!=null) pst.close();
				if(rs!=null) rs.close();
			}catch(SQLException e) {
				System.err.println("Error SQL al cerrar las estructuras.");
				System.err.println(e.getMessage());				
			}
		}
	}

	public void insertarFoto (int id, String path) {
		PreparedStatement pst=null;
		ResultSet rs=null;
		FileInputStream fis=null;
		File f=null;
		this.connect();
		try {
			pst= conn.prepareStatement("UPDATE socio SET foto = ? WHERE ID_socio=?;");
			f =new File(path);
			fis =new FileInputStream(f);	
			pst.setBinaryStream(1, fis, (int)f.length());
			pst.setInt(2, id);
			int n=pst.executeUpdate();
			if(n>0) System.out.println("Foto añadida");
			else System.out.println("Foto no añadida");
		} catch (SQLException e) {
			System.err.println("Error SQL al añadir una foto");
			System.err.println(e.getMessage());	
		} catch (FileNotFoundException e) {
			System.err.println("Error SQL al cerrar las estructuras.");
			System.err.println(e.getMessage());	
		} catch (Exception e) {
			System.err.println("Otro error al añadir una foto");
			System.err.println(e.getMessage());	
		} finally{
			try {
				if(rs!=null) rs.close();
				if(pst!=null) pst.close();
				if(fis!=null) fis.close();
			} catch (SQLException e) {
				System.err.println("Error: archivo de foto no encontrada");
				System.err.println(e.getMessage());	
			} catch(IOException e) {
				System.err.println("Error al cerrar el fichero");
				System.err.println(e.getMessage());	
			}
		}

	}

	public void variosInserts() {
		String sql1="INSERT INTO socio(id_socio, nombre, telefono, direccion) "
				+ "VALUES (10,  'Nombre1', '600000000', 'Direccion1');";

		String sql2="INSERT INTO socio(id_socio, nombre, telefono, direccion) "
				+ "VALUES (11,  'Nombre2', '600000001', 'Direccion2');";
		Statement st=null;
		connect();

		try {
			conn.setAutoCommit(false);
			st=conn.createStatement();
			int n=st.executeUpdate(sql1);
			n=st.executeUpdate(sql2);
			conn.commit();
		} catch (SQLException e) {
			System.err.println("Error al insertar varios socios: ");
			System.err.println(e.getMessage());	
			try {
				conn.rollback();
			}catch (SQLException e1) {
				System.err.println("Error critico no se garantiza la atomicidad de la transaccion: ");
				System.err.println(e1.getMessage());	
			}
		} finally {
			try {
				if (st!=null) st.close();
			} catch (SQLException e) {
				System.err.println("Error al cerrar el Statement: ");
				System.err.println(e.getMessage());	
			}
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("Error critico al reestablecer el AC a true: ");
				System.err.println(e.getMessage());	
			}
		}
	}

	public void leerFichero (String file) {
		BufferedReader br=null;
		String linea=null;
		try {
			br =new BufferedReader(new FileReader(file));
			while((linea=br.readLine()) != null) {
				System.out.println(linea);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error, no se encuentra el fichero: ");
			System.err.println(e.getMessage());	
		} catch (IOException e) {
			System.err.println("Error al leer el fichero: ");
			System.err.println(e.getMessage());;
		}finally {
			try {
				if(br!=null) br.close();
			} catch (IOException e) {
				System.err.println("Error al cerrar el fichero: ");
				System.err.println(e.getMessage());;
			}
		}

	}

}
