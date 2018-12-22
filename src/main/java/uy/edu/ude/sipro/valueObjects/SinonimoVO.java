package uy.edu.ude.sipro.valueObjects;

/*************************************************************************

Value object de interacción con la capa gráfica correspondiente a la clase Sinonimo

**************************************************************************/
public class SinonimoVO implements Comparable 
{
	private int id;
	private String nombre;	
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getNombre() 
	{
		return nombre;
	}
	
	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

	@Override
	public int compareTo(Object comparado)
	{
		return this.getNombre().toLowerCase().compareTo(((SinonimoVO)comparado).getNombre().toLowerCase());
	}
}
