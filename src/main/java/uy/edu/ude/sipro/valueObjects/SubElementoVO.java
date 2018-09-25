package uy.edu.ude.sipro.valueObjects;

public class SubElementoVO 
{	
	private int id;
	private String nombre;
	
	public SubElementoVO()
	{	}
		
 	public SubElementoVO(int id, String nombre)
 	{
 		this.id = id;
 		this.nombre = nombre;
 	}
 	
 	public int getId() { return id;	}
 	public void setId(int id) { this.id = id; }
 	
 	public String getNombre() { return nombre; }
 	public void setNombre(String nombre) { this.nombre = nombre; }
 }
