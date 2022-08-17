package Practica2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SeriesDatabase {
	private Connection conn;

	public boolean openConnection(){
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
			System.err.println("Error al abrir la conexión: ");
			System.err.println(e.getMessage());
		}
		return abierto;
	}

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

}
