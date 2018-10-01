package uy.edu.ude.sipro.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Correctores", uniqueConstraints = {@UniqueConstraint(name = "uq_nombre_Corrector", columnNames = "nombre")})
public class Corrector
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdCorrector")
	protected int id;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "Nombre")
	protected String nombre;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IdProyecto")
	private Proyecto proyecto;
	
	public Corrector() 
	{	}
	
	public Corrector(String nombre, Proyecto proyecto)
	{
		this.nombre = nombre;
		this.proyecto = proyecto;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getNombre() {	return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public Proyecto getProyecto() { return proyecto; }
	public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }
}