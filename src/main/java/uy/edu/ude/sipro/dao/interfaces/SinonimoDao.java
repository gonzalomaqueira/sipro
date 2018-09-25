package uy.edu.ude.sipro.dao.interfaces;

import java.util.List;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Sinonimo;

public interface SinonimoDao
{
	void agregar(Sinonimo sinonimo);
	void modificar(Sinonimo sinonimo);
	void eliminar(Sinonimo sinonimo);
	List<Sinonimo> obtenerSinonimos();
	Sinonimo obtenerSinonimoPorId(int id);
}
