package uy.edu.ude.sipro.busquedas;

import java.util.ArrayList;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;


public interface BusquedaService {
	
	ArrayList<Elemento> obtenerElementoString(String busqueda);
	boolean altaProyectoES(Proyecto proyecto, String[] textoOriginal) throws Exception;
	boolean bajaProyectoES(int idProyecto ) throws Exception;
	ArrayList<ResultadoBusqueda> realizarBusquedaES(String busqueda) throws Exception;	
}
