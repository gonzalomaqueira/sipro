package uy.edu.ude.sipro.busquedas;

import java.util.ArrayList;

import uy.edu.ude.sipro.entidades.Elemento;


public interface BusquedaService {
	
	ArrayList<Elemento> obtenerElementoString(String busqueda);
	ArrayList<ResultadoBusqueda> buscarElementosProyectoES(ArrayList<Elemento> elementos) throws Exception;	
}
