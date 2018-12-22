package uy.edu.ude.sipro.busquedas;

import java.util.ArrayList;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
/*************************************************************************
 
Interface encargada de la lógica asociada a las búsquedas
	
**************************************************************************/
public interface BusquedaService
{
	boolean altaProyectoES(Proyecto proyecto, String[] textoOriginal) throws Exception;
	boolean bajaProyectoES(int idProyecto ) throws Exception;
	ArrayList<ResultadoBusqueda> realizarBusquedaES(String busqueda, DatosFiltro datosFiltro) throws Exception;
	boolean actualizarSinonimosElemntosES(ArrayList<Elemento> elementos) throws Exception;
	boolean abrirCerrarIndiceES(boolean abrirConexion) throws Exception;
	ArrayList<Integer> obtenerListaProyectosES() throws Exception;
	void creacionIncideES() throws Exception;
}
