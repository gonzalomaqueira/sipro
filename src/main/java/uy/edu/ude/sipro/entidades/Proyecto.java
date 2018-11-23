package uy.edu.ude.sipro.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.utiles.Constantes;
import uy.edu.ude.sipro.utiles.FuncionesTexto;
import uy.edu.ude.sipro.utiles.SeccionTexto;

@Entity
@Table(name = "Proyectos", uniqueConstraints = {@UniqueConstraint(name = "uq_codigoUde_Proyectos", columnNames = "codigoUde")})
public class Proyecto
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IdProyecto")
	private int id;

	@NotNull
	@Column(name = "CodigoUde")
	private String codigoUde;

	@Size(min = 1, max = 255)
	@Column(name = "Titulo")
	private String Titulo;
	
	@Column(name = "Anio")
	private int anio;

	@Size(min = 0, max = 255)
	@Column(name = "Carrera")
	private String carrera;
	
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="proyectosComoCorrector", fetch = FetchType.EAGER) 
	private Set<Docente> correctores;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IdTutor")
	private Docente tutor;

	@Lob
	@Column(name = "Tutor")
	private ArrayList<String> tutorString;
    
	@Column(name = "Nota")
	private int nota;

	@Lob
	@Column(name = "Alumnos")
	private ArrayList<String> alumnos;

	@NotNull
	@Column(name = "RutaArchivo")
	private String rutaArchivo;

	@Column(name = "Resumen", columnDefinition="TEXT")
	private String resumen;

	@NotNull
	@Column(name = "FechaAlta")
	private Date fechaAlta;

	@NotNull
	@Column(name = "FechaUltimaModificacion")
	private Date fechaUltimaModificacion;

	@Enumerated(EnumType.STRING)
	private EstadoProyectoEnum estado;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "RelProyectoElemento", joinColumns = { @JoinColumn(name = "idProyecto") }, inverseJoinColumns = { @JoinColumn(name = "idElemento") })
	private Set<Elemento> elementosRelacionados;
	
	@Transient
	private List<SeccionTexto> DocumentoPorSecciones;

	public Proyecto()
	{
		this.estado = EstadoProyectoEnum.SIN_PROCESAR;
		Date vfecha = new Date();
		this.fechaAlta = vfecha;
		this.fechaUltimaModificacion = vfecha;
	}

	public Proyecto(String codigoUde, String carrera, Set<Docente> correctores,  int nota, String rutaArchivo)
	{
		this();
		this.codigoUde = codigoUde;
		this.carrera = carrera;
		this.correctores = correctores;
		this.nota = nota;
		this.rutaArchivo = rutaArchivo;
	}

	public Proyecto(String codigoUde, String titulo, int anio, String carrera, Set<Docente> correctores, int nota, 
			ArrayList<String> alumnos, Docente tutor, String rutaArchivo, String resumen)
	{
		this(codigoUde, carrera, correctores, nota, rutaArchivo);
		this.Titulo = titulo;
		this.anio = anio;
		this.carrera = carrera;
		this.alumnos = alumnos;
		this.tutor = tutor;		
		this.correctores = correctores;
		this.resumen = resumen;
	}

	public int getId() { return id; }
	public void setId(int id) {	this.id = id; }
	
	public String getCodigoUde() { return codigoUde; }
	public void setCodigoUde(String codigoUde) { this.codigoUde = codigoUde; }

	public String getTitulo() { return Titulo; }
	public void setTitulo(String titulo) { Titulo = titulo; }
	
	public int getAnio() { return anio;	}
	public void setAnio(int anio) { this.anio = anio; }

	public Docente getTutor() {	return tutor; }
	public void setTutor(Docente tutor) { this.tutor = tutor; }
	
	public ArrayList<String> getTutorString() {	return tutorString;	}
	public void setTutorString(ArrayList<String> tutorString) {	this.tutorString = tutorString;	}

	public String getCarrera() { return carrera; }
	public void setCarrera(String carrera) { this.carrera = carrera; }
	
	public Set<Docente> getCorrectores() { return correctores;	}
	public void setCorrectores(Set<Docente> correctores) {this.correctores = correctores;	}

	public int getNota() { return nota; }
	public void setNota(int nota) { this.nota = nota; }
	
	public ArrayList<String> getAlumnos() { return alumnos; }
	public void setAlumnos(ArrayList<String> alumnos) { this.alumnos = alumnos; }

	public String getRutaArchivo() { return rutaArchivo; }
	public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo;}

	public String getResumen() { return resumen; }
	public void setResumen(String resumen) { this.resumen = resumen; }

	public Date getFechaAlta() { return fechaAlta; }
	public void setFechaAlta(Date fechaAlta) { this.fechaAlta = fechaAlta; }

	public Date getFechaUltimaModificacion() { return fechaUltimaModificacion; }
	public void setFechaUltimaModificacion(Date fechaUltimaModificacion) { this.fechaUltimaModificacion = fechaUltimaModificacion; }
	
	public Set<Elemento> getElementosRelacionados() { return elementosRelacionados; }
	public void setElementosRelacionados(Set<Elemento> elementosRelacionados) { this.elementosRelacionados = elementosRelacionados; }

	public EstadoProyectoEnum getEstado() { return estado; }
	public void setEstado(Enumerados.EstadoProyectoEnum estado) 
	{
		this.estado = estado;
		this.fechaUltimaModificacion = new Date();
	}

	public List<SeccionTexto> getDocumentoPorSecciones() { return DocumentoPorSecciones; }
	public void setDocumentoPorSecciones(List<SeccionTexto> documentoPorSecciones) { DocumentoPorSecciones = documentoPorSecciones; }
	
	/** MÉTODOS **/
	
	public ArrayList<String> devolverResumen() 
	{
		if( this.DocumentoPorSecciones!=null ) 
		{
			SeccionTexto secResumen=null;
			for(SeccionTexto sec : this.DocumentoPorSecciones)
			{
				if(sec != null && (FuncionesTexto.esTituloResumen(sec.titulo)))
				{
					secResumen= sec;
					break;
				}
			}
			if( secResumen!=null ) 
			{
				return limpiarResumen(secResumen.getContenido());
			}
		}
		return null;
	}

	private ArrayList<String> limpiarResumen(ArrayList<String> contenido)
	{
		int largoList = contenido.size();
		for(int x=largoList-1; x>=0; x--)
		{			
			if( !FuncionesTexto.terminaPunto (contenido.get(x)) )
				contenido.remove(x);
			else
				break;
		}
		for(int x=0; x<contenido.size(); x++)
		{
			if(contenido.get(x).trim().equals(""))
			{
				contenido.remove(x);
				x--;
			}
			else
				break;
		}
		return contenido;
	}
	
	public ArrayList<String> devolverAlumnos () 
	{
		if( this.DocumentoPorSecciones!=null ) 
		{
			SeccionTexto secAlumnos=null;
			for(SeccionTexto sec : this.DocumentoPorSecciones)
			{
				if(sec != null && FuncionesTexto.esTituloAlumnos(sec.getTitulo()))
				{
					secAlumnos = sec;
					break;
				}
			}
			if(secAlumnos!=null)
			{
				return limpiarAlumnos(secAlumnos.getContenido());
			}
		}
		return null;
	}
	
	private ArrayList<String> limpiarAlumnos(ArrayList<String> contenido)
	{		
		contenido = FuncionesTexto.eliminarLineasVacias(contenido);
		contenido = FuncionesTexto.eliminarEspacios(contenido);
		contenido = FuncionesTexto.separarAlumnos(contenido);
		
		return contenido;
	}
	
	public ArrayList<String> devolverTutor()
	{
		if( this.DocumentoPorSecciones!=null ) 
		{
			SeccionTexto secTutor=null;
			for(SeccionTexto sec : this.DocumentoPorSecciones)
			{
				if(sec != null && FuncionesTexto.esTituloTutor(sec.getTitulo()))
				{
					secTutor = sec;
					break;
				}
			}
			if(secTutor!=null)
			{
				return limpiarTutor(secTutor.getContenido());
			}
		}
		return null;
	}
	
	private ArrayList<String> limpiarTutor(ArrayList<String> contenido)
	{		
		contenido = FuncionesTexto.eliminarLineasVacias(contenido);
		contenido = FuncionesTexto.eliminarEspacios(contenido);
		
		return contenido;
	}
	
	public String devolverTitulo(ArrayList<String> contenido) 
	{
		String retorno = "";
		for(String linea : contenido)
		{
			if (FuncionesTexto.esTituloAlumnos(linea) ||
				FuncionesTexto.esTituloTutor(linea))
			{
				break;
			}
			if (!FuncionesTexto.esFechaDocumento(linea)
				&& !linea.trim().equalsIgnoreCase("1")
				&& !linea.trim().equalsIgnoreCase("pag. 1")
				&& !linea.trim().equalsIgnoreCase("pag 1")
				&& !linea.trim().equalsIgnoreCase("pág. 1")
				&& !linea.trim().equalsIgnoreCase("pág 1")
				&& !linea.trim().equalsIgnoreCase("página 1"))
			{
				retorno = retorno + " " + linea;
			}
		}
		int largo = retorno.length();
		if (retorno.length() > Constantes.LARGO_TITULO_MAX)
		{
			largo = Constantes.LARGO_TITULO_MAX;
		}
		return retorno.substring(0, largo).trim();
	}

	
	// Hacer:
	
	public String getContenido()
	{
		return this.getResumen();
	}

	public ArrayList<String> getListaStringElementos() 
	{
		Set<String> retorno = new HashSet<String>();
		
		if (this.getElementosRelacionados() != null)
		{
			for (Elemento elem: this.getElementosRelacionados()) 
			{
				retorno.add(elem.getNombre());
			}
		}
		
		return new ArrayList(retorno);
	}

	public String devolverInicioResumen()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
