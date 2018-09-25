package uy.edu.ude.sipro.entidades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
	TipoElemento tipoElemento;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinTable(name="RelacionesElementos", joinColumns={@JoinColumn(name="IdOrigen")},
                inverseJoinColumns={@JoinColumn(name="IdRelacionado")})
    private List<Elemento> elementosRelacionados = new ArrayList<Elemento>();
 
    @ManyToMany(mappedBy="elementosRelacionados", fetch = FetchType.EAGER)
    private List<Elemento> elementosOrigen = new ArrayList<Elemento>();
	
    @OneToMany(mappedBy="elemento", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Sinonimo> sinonimos;
	 
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="elementosRelacionados", fetch = FetchType.LAZY) 
	private List<Proyecto> proyectos;
	
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
				    List<Elemento> elementosRelacionados, List<Sinonimo> sinonimos) 
	{
		this(nombre, esCategoria, tipoElemento);
		this.elementosRelacionados = elementosRelacionados;
		this.sinonimos = sinonimos;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public boolean isEsCategoria() { return esCategoria; }
	public void setEsCategoria(boolean esCategoria) { this.esCategoria = esCategoria; }

	public Enumerados.TipoElemento getTipoElemento() { return tipoElemento;	}
	public void setTipoElemento(Enumerados.TipoElemento tipoElemento) { this.tipoElemento = tipoElemento; }

	public List<Elemento> getElementosOrigen() { return elementosOrigen; }
	public void setElementosOrigen(List<Elemento> elementosOrigen) { this.elementosOrigen = elementosOrigen; }

	public List<Elemento> getElementosRelacionados() { return elementosRelacionados; }
	public void setElementosRelacionados(List<Elemento> elementoRelacionados) { this.elementosRelacionados = elementoRelacionados; }

	public List<Sinonimo> getSinonimos() { return sinonimos; }
	public void setSinonimos(List<Sinonimo> sinonimos) { this.sinonimos = sinonimos; }

	public List<Proyecto> getProyectos() { return proyectos; }
	public void setProyectos(List<Proyecto> proyectos) { this.proyectos = proyectos; }


}
