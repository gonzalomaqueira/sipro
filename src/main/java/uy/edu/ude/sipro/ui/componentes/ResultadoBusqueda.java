package uy.edu.ude.sipro.ui.componentes;

public class ResultadoBusqueda
{
	private int idProyecto;
	private String tituloProyecto;
	private String resumenBusqueda;
	private int ordenResultado;

	public ResultadoBusqueda(int idProyecto, String tituloProyecto, String resumenBusqueda, int ordenResultado) 
	{
		this.idProyecto = idProyecto;
		this.tituloProyecto = tituloProyecto;
		this.resumenBusqueda = resumenBusqueda;
		this.ordenResultado = ordenResultado;
	}
	
	public int getIdProyecto() {
		return idProyecto;
	}
	public void setIdProyecto(int idProyecto) {
		this.idProyecto = idProyecto;
	}
	public String getTituloProyecto() {
		return tituloProyecto;
	}
	public void setTituloProyecto(String tituloProyecto) {
		this.tituloProyecto = tituloProyecto;
	}
	public String getResumenBusqueda() {
		return resumenBusqueda;
	}
	public void setResumenBusqueda(String resumenBusqueda) {
		this.resumenBusqueda = resumenBusqueda;
	}
	public int getOrdenResultado() {
		return ordenResultado;
	}
	public void setOrdenResultado(int ordenResultado) {
		this.ordenResultado = ordenResultado;
	}
}