package uy.edu.ude.sipro.dao.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Elemento;

/*************************************************************************

Interface encargada de la persistencia de Elementos

**************************************************************************/
public interface ElementoDao 
{	
	void agregar(Elemento elemento);
	void modificar(Elemento elemento);
	void eliminar(Elemento elemento);
	Set<Elemento> obtenerElementos();
	Elemento obtenerElementoPorId(int id);
}
