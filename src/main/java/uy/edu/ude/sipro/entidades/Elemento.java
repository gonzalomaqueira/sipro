package uy.edu.ude.sipro.entidades;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;

/*************************************************************************

Clase que define la entidad Elemento

**************************************************************************/
@Entity
@Table(name = "Elemento", uniqueConstraints = {@UniqueConstraint(name = "uq_nombre_Elemento", columnNames = "nombre")})
public class Elemento
{	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private int id;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "Nombre")
	private String nombre;
	
	@NotNull
	@Column(name= "EsCategoria")
	boolean esCategoria;
	
	@NotNull
	@Column(name= "TipoElemento")
	@Enumerated(EnumType.STRING)
	TipoElemento tipoElemento;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinTable(name="RelacionesElementos", joinColumns={@JoinColumn(name="IdOrigen")},
                inverseJoinColumns={@JoinColumn(name="IdRelacionado")})
    private Set<Elemento> elementosRelacionados = new HashSet<Elemento>();
 
    @ManyToMany(mappedBy="elementosRelacionados", fetch = FetchType.EAGER)
    private Set<Elemento> elementosOrigen = new HashSet<Elemento>();
	
    @OneToMany(mappedBy="elemento", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Sinonimo> sinonimos;
	 
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="elementosRelacionados", fetch = FetchType.EAGER) 
	private Set<Proyecto> proyectos;
	
	public Elemento() 
	{	}
	
	public Elemento(String nombre, boolean esCategoria, TipoElemento tipoElemento) 
	{
		this.nombre = nombre;
		this.esCategoria = esCategoria;
		this.tipoElemento = tipoElemento;
	}
	
	public Elemento(int id, String nombre, boolean esCategoria, TipoElemento tipoElemento) 
	{
		this.id=id;
		this.nombre = nombre;
		this.esCategoria = esCategoria;
		this.tipoElemento = tipoElemento;
	}
	
	public Elemento(String nombre, boolean esCategoria, TipoElemento tipoElemento,
				    Set<Elemento> elementosRelacionados, Set<Sinonimo> sinonimos) 
	{
		this(nombre, esCategoria, tipoElemento);
		this.elementosRelacionados = elementosRelacionados;
		this.sinonimos = sinonimos;
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

	public Enumerados.TipoElemento getTipoElemento() {
		return tipoElemento;
	}

	public void setTipoElemento(Enumerados.TipoElemento tipoElemento) {
		this.tipoElemento = tipoElemento;
	}

	public Set<Elemento> getElementosOrigen() {
		return elementosOrigen;
	}

	public void setElementosOrigen(Set<Elemento> elementosOrigen) {
		this.elementosOrigen = elementosOrigen;
	}

	public Set<Elemento> getElementosRelacionados() {
		return elementosRelacionados;
	}

	public void setElementosRelacionados(Set<Elemento> elementoRelacionados) {
		this.elementosRelacionados = elementoRelacionados;
	}

	public Set<Sinonimo> getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(Set<Sinonimo> sinonimos) {
		this.sinonimos = sinonimos;
	}

	public Set<Proyecto> getProyectos() {
		return proyectos;
	}

	public void setProyectos(Set<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}
}
