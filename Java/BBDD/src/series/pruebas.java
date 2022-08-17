package series;

public class pruebas {
	
	public static void main (String[] args) {
		SeriesDatabase ej1= new SeriesDatabase();
		System.out.println("Conexion a la base de datos: "+ej1.openConnection());
		System.out.println("Creacion tabla capitulo: "+ej1.createTableCapitulo());
		System.out.println("Creacion tabla valor: "+ej1.createTableValora());
		System.out.println("Carga de capitulo: "+ej1.loadCapitulos("capitulos.csv"));
		System.out.println("Carga de valoraciones: "+ej1.loadValoraciones("valoraciones.csv"));
		System.out.println(ej1.catalogo());
		System.out.println(ej1.noHanComentado());
		System.out.println(ej1.mediaGenero("Comedia"));
		System.out.println(ej1.duracionMedia("Ingles"));
		System.out.println(ej1.setFoto("HomerSimpson.jpg"));
		System.out.println("Desconexion a la base de datos: "+ej1.closeConnection());	
	}
	
}
