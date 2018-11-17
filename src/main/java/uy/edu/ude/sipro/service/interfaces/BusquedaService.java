package uy.edu.ude.sipro.service.interfaces;

import java.util.ArrayList;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;

public interface BusquedaService {
	
	ArrayList<Elemento> obtenerElementoString(String busqueda);
	String buscarElementosProyectoES(ArrayList<Elemento> elementos) throws Exception;	

}
