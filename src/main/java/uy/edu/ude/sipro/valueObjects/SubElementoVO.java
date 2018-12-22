package uy.edu.ude.sipro.valueObjects;

/*************************************************************************

Value object de interacción con la capa gráfica para un subElemento

**************************************************************************/
public class SubElementoVO implements Comparable
{	
	private int id;
	private String nombre;
	
	public SubElementoVO()
	{
	}
		
 	public SubElementoVO(int id, String nombre)
 	{
 		this.id = id;
 		this.nombre = nombre;
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

	@Override
	public int compareTo(Object comparado)
	{
		return this.getNombre().toLowerCase().compareTo(((SubElementoVO)comparado).getNombre().toLowerCase());
	}
 }
