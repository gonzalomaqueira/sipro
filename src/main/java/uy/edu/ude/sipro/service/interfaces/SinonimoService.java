package uy.edu.ude.sipro.service.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Sinonimo;

/*************************************************************************

Interface encargada de la lógica de negocio para Sinónimos

**************************************************************************/
public interface SinonimoService
{
	void agregar(String nombre, Elemento elemento);
	void modificar(int id, String nombre);
	void eliminar(int id);
	Set<Sinonimo> obtenerSinonimos();
    Sinonimo obtenerSinonimoPorId(int id);
}
