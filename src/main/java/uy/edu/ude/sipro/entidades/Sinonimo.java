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

/*************************************************************************

Clase que define la entidad Sinonimo

**************************************************************************/
@Entity
@Table(name = "SinonimosElemento", uniqueConstraints = {@UniqueConstraint(name = "uq_nombre_SinonimoElemento", columnNames = "nombre")})
public class Sinonimo
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdSinonimo")
	protected int id;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "Nombre")
	protected String nombre;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IdElemento")
	private Elemento elemento;
	
	public Sinonimo() 
	{	}
	
	public Sinonimo(String nombre, Elemento elemento)
	{
		this.nombre = nombre;
		this.elemento = elemento;
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

	public Elemento getElemento() {
		return elemento;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}
}