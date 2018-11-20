package uy.edu.ude.sipro.busquedas;

import java.util.ArrayList;
import java.util.Iterator;

import uy.edu.ude.sipro.utiles.Constantes;

public class ResultadoBusqueda
{
	private int idProyecto;
	private String tituloProyecto;
	private float score;
	private String codigoUde;
	private int anio;
	private ArrayList<String> highlight;

	public ResultadoBusqueda() 
	{
		
	}
	
	public ResultadoBusqueda(int idProyecto, String tituloProyecto, float score, String codigoUde, ArrayList<String> highlight) 
	{
		this.idProyecto = idProyecto;
		this.tituloProyecto = tituloProyecto;
		this.score = score;
		this.codigoUde = codigoUde;
		this.highlight = highlight;
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

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getCodigoUde() {
		return codigoUde;
	}

	public void setCodigoUde(String codigoUde) {
		this.codigoUde = codigoUde;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public ArrayList<String> getHighlight() {
		return highlight;
	}

	public void setHighlight(ArrayList<String> highlight) {
		this.highlight = highlight;
	}
	
	
	public String getResumenBusqueda()
	{
		String resumen = "";
		
		int i=0;
		Iterator<String> iterador = this.highlight.iterator();
		while(iterador.hasNext() && i < Constantes.CANTIDAD_DETALLES_BUSQUEDA)
		{
			String valor = iterador.next();
			resumen= resumen  + " ..... " +  valor.replaceAll("<em>", "<b>").replaceAll("</em>", "</b>");
			i++;
		}
		resumen = resumen + " ..... ";
		
		return resumen;
	}
	

}