package uy.edu.ude.sipro.dao.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Docente;

/*************************************************************************

Interface encargada de la persistencia de Docentes

**************************************************************************/
public interface DocenteDao 
{
	void agregar(Docente docente);
	void modificar(Docente docente);
	void eliminar(Docente docente);
	Set<Docente> obtenerDocentes();
	Docente obtenerDocentePorId(int id);
	boolean existeDocente(String nombre, String apellido);
}
