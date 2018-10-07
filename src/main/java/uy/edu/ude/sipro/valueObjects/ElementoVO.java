package uy.edu.ude.sipro.valueObjects;

import java.util.List;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;

public class ElementoVO 
{	
	private int id;
	private String nombre;
	private boolean esCategoria;
	private TipoElemento tipoElemento;
	private Set<SinonimoVO> sinonimos;
	private Set<SubElementoVO> elementosRelacionados;
	
	public ElementoVO()
	{	}
		
 	public ElementoVO(String nombre)
 	{
 		this.nombre = nombre;
 	}
 	
 	public int getId() { return id;	}
 	public void setId(int id) { this.id = id; }
 	
 	public String getNombre() { return nombre; }
 	public void setNombre(String nombre) { this.nombre = nombre; }
 	
 	public boolean isEsCategoria() { return esCategoria;	}
	public void setEsCategoria(boolean esCategoria) { this.esCategoria = esCategoria; }
	
	

	public TipoElemento getTipoElemento() {	return tipoElemento;}
	public void setTipoElemento(TipoElemento tipoElemento) {this.tipoElemento = tipoElemento;	}

	public Set<SinonimoVO> getSinonimos() { return sinonimos; }
 	public void setSinonimos(Set<SinonimoVO> sinonimos) { this.sinonimos = sinonimos; }
 	
 	public Set<SubElementoVO> getElementosRelacionados() { return elementosRelacionados; }
 	public void setElementosRelacionados(Set<SubElementoVO> elementosRelacionados) { this.elementosRelacionados = elementosRelacionados; }
 }
