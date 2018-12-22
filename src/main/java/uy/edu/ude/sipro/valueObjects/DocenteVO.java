package uy.edu.ude.sipro.valueObjects;

/*************************************************************************

Value object de interacción con la capa gráfica correspondiente a la clase Docente

**************************************************************************/
public class DocenteVO implements Comparable
{
	private int id;
	private String nombre;
	private String apellido;
	
	public DocenteVO() {
	}
	
	public DocenteVO(String nombre, String apellido) {
		this.nombre = nombre;
		this.apellido = apellido;
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

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombreCompleto() {
		return this.nombre + " " + this.apellido;
	}

	@Override
	public int compareTo(Object comparado)
	{
		return this.getId() - ((DocenteVO) comparado).getId();
	}
}
