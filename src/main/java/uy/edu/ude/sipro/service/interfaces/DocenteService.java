package uy.edu.ude.sipro.service.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Docente;

/*************************************************************************

Interface encargada de la lógica de negocio para Docentes

**************************************************************************/
public interface DocenteService 
{
	void agregar(String nombre, String apellido);
	void modificar(int id, String nombre, String apellido);
	void eliminar(int id);
    Set<Docente> obtenerDocentes();
    Docente obtenerDocentePorId(int id);
    boolean existeDocente(String nombre, String apellido);
}
