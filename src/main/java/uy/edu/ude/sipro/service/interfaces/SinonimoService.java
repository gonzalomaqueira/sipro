package uy.edu.ude.sipro.service.interfaces;

import java.util.List;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Sinonimo;

public interface SinonimoService
{
	void agregar(String nombre, Elemento elemento);
	void modificar(int id, String nombre);
	void eliminar(int id);
    List<Sinonimo> obtenerSinonimos();
    Sinonimo obtenerSinonimoPorId(int id);
}
