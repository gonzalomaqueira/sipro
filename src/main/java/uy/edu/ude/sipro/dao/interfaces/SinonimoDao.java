package uy.edu.ude.sipro.dao.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Sinonimo;

/*************************************************************************

Interface encargada de la persistencia de Sinónimos de elementos

**************************************************************************/
public interface SinonimoDao
{
	void agregar(Sinonimo sinonimo);
	void modificar(Sinonimo sinonimo);
	void eliminar(Sinonimo sinonimo);
	Set<Sinonimo> obtenerSinonimos();
	Sinonimo obtenerSinonimoPorId(int id);
}
