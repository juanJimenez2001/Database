package Main;

import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	
	public static void main (String[] args) {
		Ejemplo1 ej1= new Ejemplo1();
		try {
			//ej1.connect();
			//ej1.queryCervezas();
			//ej1.createTableEmpleado();
			//ej1.updateNombreSocios();
			//ej1.getSOcio(2);
			//ej1.getDBMetaData();
			//ej1.getTableMetadata();
			//ej1.insertarFoto(1, "HomerSimpson.jpg");
			//ej1.variosInserts();
			ej1.leerFichero("inserts.csv");
			ej1.disconnect();
		}
		catch(SQLException e){
			System.err.println("Error al conectar a la base de datos");
		}
	}
}
