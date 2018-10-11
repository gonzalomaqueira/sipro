package uy.edu.ude.sipro.valueObjects;

public class PerfilVO implements Comparable
{

	private int Id;	
	private String descripcion;
	
	public int getId() 
	{
		return Id;
	}

	public void setId(int id) 
	{
		Id = id;
	}
	
	public String getDescripcion()
	{
		return descripcion;
	}
	
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	@Override
	public int compareTo(Object comparado)
	{
		return this.getId() - ((PerfilVO) comparado).getId();
	}
}
