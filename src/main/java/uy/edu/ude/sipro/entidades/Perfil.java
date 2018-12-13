package uy.edu.ude.sipro.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Perfiles")
public class Perfil
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdPerfil")
	private int Id;
	
	@Column(name="Descripcion")
	private String descripcion;
	
	public Perfil (int id, String desc)
	{
		this.Id=id;
		this.descripcion = desc;
	}

	public Perfil()
	{
	}
	
	public int getId() { return Id; }
	public void setId(int id) { Id = id; }
	
	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
