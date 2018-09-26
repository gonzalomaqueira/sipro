package uy.edu.ude.sipro.service.interfaces;

import java.util.List;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;

public interface ElementoService
{	
	void agregar(Elemento elemento);
	void agregar(String nombre, boolean esCategoria, TipoElemento tipoElemento, List<SubElementoVO> elementosRelacionados, List<SinonimoVO> sinonimos);
	void modificar(int id, String nombre, boolean esCategoria, TipoElemento tipoElemento, List<SubElementoVO> elementosRelacionados, List<SinonimoVO> sinonimos);
	void eliminar(int id);
	void eliminar(Elemento elemento);
	List<ElementoVO> obtenerElementos();
	Elemento obtenerElementoPorId(int id);
}