package uy.edu.ude.sipro.valueObjects;

import java.util.List;

import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;

/*************************************************************************

Value object de interacción con la capa gráfica correspondiente a la clase Elemento

**************************************************************************/
public class ElementoVO implements Comparable
{	
	private int id;
	private String nombre;
	private boolean esCategoria;
	private TipoElemento tipoElemento;
	private List<SinonimoVO> sinonimos;
	private List<SubElementoVO> elementosRelacionados;
	
	public ElementoVO()
	{		
	}
		
 	public ElementoVO(String nombre)
 	{
 		this.nombre = nombre;
 	}
 	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isEsCategoria() {
		return esCategoria;
	}

	public void setEsCategoria(boolean esCategoria) {
		this.esCategoria = esCategoria;
	}

	public TipoElemento getTipoElemento() {
		return tipoElemento;
	}

	public void setTipoElemento(TipoElemento tipoElemento) {
		this.tipoElemento = tipoElemento;
	}

	public List<SinonimoVO> getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(List<SinonimoVO> sinonimos) {
		this.sinonimos = sinonimos;
	}

	public List<SubElementoVO> getElementosRelacionados() {
		return elementosRelacionados;
	}

	public void setElementosRelacionados(List<SubElementoVO> elementosRelacionados) {
		this.elementosRelacionados = elementosRelacionados;
	}

	@Override
	public int compareTo(Object comparado)
	{
		return this.getNombre().toLowerCase().compareTo(((ElementoVO)comparado).getNombre().toLowerCase());
	}
 }
