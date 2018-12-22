package uy.edu.ude.sipro.service.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;

/*************************************************************************

Interface encargada de la lógica de negocio para Elementos

**************************************************************************/
public interface ElementoService
{	
	void agregar(Elemento elemento);	
	void modificar(int id, String nombre, boolean esCategoria, TipoElemento tipoElemento, Set<SubElementoVO> elementosRelacionados, Set<SinonimoVO> sinonimos);
	void eliminar(int id);
	void eliminar(Elemento elemento);
	void altaElemento(String nombre, boolean esCategoria, TipoElemento tipoElemento, Set<SubElementoVO> elementosRelacionados, Set<SinonimoVO> sinonimos);
	Set<Elemento> obtenerElementos();
	Elemento obtenerElementoPorId(int id);
}