package uy.edu.ude.sipro.entidades;

import java.util.List;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*************************************************************************

Clase que define la entidad Docente

**************************************************************************/
@Entity
public class Docente
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdDocente")
	protected int id;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "Nombre")
	protected String nombre;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "Apellido")
	protected String apellido;
	
	@OneToMany(mappedBy="tutor", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Proyecto> proyectosComoTutor;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "RelProyectoCorrectores", joinColumns = { @JoinColumn(name = "idDocente") }, inverseJoinColumns = { @JoinColumn(name = "idProyecto") })
	private List<Proyecto> proyectosComoCorrector;
	
	public Docente()
	{		
	}
	
	public Docente(String nombre, String apellido)
	{
		this.nombre = nombre;
		this.apellido = apellido;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Proyecto> getProyectosComoTutor() {
		return proyectosComoTutor;
	}

	public void setProyectosComoTutor(List<Proyecto> proyectosComoTutor) {
		this.proyectosComoTutor = proyectosComoTutor;
	}

	public List<Proyecto> getProyectosComoCorrector() {
		return proyectosComoCorrector;
	}

	public void setProyectosComoCorrector(List<Proyecto> proyectosComoCorrector) {
		this.proyectosComoCorrector = proyectosComoCorrector;
	}

	public String getNombreCompleto() {
		return this.nombre + " " + this.apellido;
	}
}