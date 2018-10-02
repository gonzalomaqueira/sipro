package uy.edu.ude.sipro.service.interfaces;

import java.util.List;

import uy.edu.ude.sipro.entidades.Docente;

public interface DocenteService 
{
	void agregar(String nombre, String apellido);
	void modificar(int id, String nombre, String apellido);
	void eliminar(int id);
    List<Docente> obtenerDocentes();
    Docente obtenerDocentePorId(int id);
}
