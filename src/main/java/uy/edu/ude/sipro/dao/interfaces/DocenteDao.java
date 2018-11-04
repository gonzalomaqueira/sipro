package uy.edu.ude.sipro.dao.interfaces;

import java.util.List;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Docente;

public interface DocenteDao 
{
	void agregar(Docente docente);
	void modificar(Docente docente);
	void eliminar(Docente docente);
	Set<Docente> obtenerDocentes();
	Docente obtenerDocentePorId(int id);
	boolean existeDocente(String nombre, String apellido);
}
