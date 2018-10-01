package uy.edu.ude.sipro.valueObjects;

public class CorrectorVO 
{
	private int id;
	private String nombre;
	
	public CorrectorVO() {

	}
	
	public CorrectorVO(String nombre) {
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
	
}
