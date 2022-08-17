package Practica2;

public class Main {
	
	public static void main (String[] args) {
		SeriesDatabase ej1= new SeriesDatabase();
		System.out.println("Conexion a la base de datos: "+ej1.openConnection());
		System.out.println("Desconexion a la base de datos: "+ej1.closeConnection());
		System.out.println("Desconexion a la base de datos: "+ej1.closeConnection());
	}
	
}
