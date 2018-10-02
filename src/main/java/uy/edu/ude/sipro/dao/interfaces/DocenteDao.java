package uy.edu.ude.sipro.dao.interfaces;

import java.util.List;

import uy.edu.ude.sipro.entidades.Docente;

public interface DocenteDao 
{
	void agregar(Docente docente);
	void modificar(Docente docente);
	void eliminar(Docente docente);
	List<Docente> obtenerDocentes();
	Docente obtenerDocentePorId(int id);
}
