package uy.edu.ude.sipro.valueObjects;

public class ElementoReporteVO {
	
	private String nombreElemento;
	private int cantidad;
	private float porcentaje;	
	
	public ElementoReporteVO(String nombreElemento, int cantidad)
	{		
		this.nombreElemento = nombreElemento;
		this.cantidad = cantidad;
	}
	
	public String getNombreElemento() {
		return nombreElemento;
	}
	public void setNombreElemento(String nombreElemento) {
		this.nombreElemento = nombreElemento;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public float getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(float porcentaje) {
		this.porcentaje = porcentaje;
	}
}
