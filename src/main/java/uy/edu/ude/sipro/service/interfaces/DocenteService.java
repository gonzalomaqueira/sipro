package uy.edu.ude.sipro.service.interfaces;

import java.util.List;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Docente;

public interface DocenteService 
{
	void agregar(String nombre, String apellido);
	void modificar(int id, String nombre, String apellido);
	void eliminar(int id);
    Set<Docente> obtenerDocentes();
    Docente obtenerDocentePorId(int id);
}
