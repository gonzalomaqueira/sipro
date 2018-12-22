package uy.edu.ude.sipro.valueObjects;

/*************************************************************************

Value object de interacción con la capa gráfica correspondiente a la clase ElementoReporte

**************************************************************************/
public class ElementoReporteVO implements Comparable  
{
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
	
	@Override
	public int compareTo(Object comparado)
	{
		return ((ElementoReporteVO) comparado).getCantidad() - this.getCantidad();
	}
}
