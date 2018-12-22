package uy.edu.ude.sipro.busquedas;

import java.util.ArrayList;
import java.util.Iterator;

import uy.edu.ude.sipro.utiles.Constantes;

/*************************************************************************

Clase que contiene los atributos necesarios para devolver un resultado de búsqueda

**************************************************************************/
public class ResultadoBusqueda
{
	private int idProyecto;
	private String tituloProyecto;
	private float score;
	private String codigoUde;
	private int anio;
	private int nota;
	private ArrayList<String> highlight;
	private String abstractProyecto;

	public ResultadoBusqueda() 
	{		
	}
	
	public ResultadoBusqueda(int idProyecto, String tituloProyecto, float score, String codigoUde, ArrayList<String> highlight, String abstractProyecto) 
	{
		this.idProyecto = idProyecto;
		this.tituloProyecto = tituloProyecto;
		this.score = score;
		this.codigoUde = codigoUde;
		this.highlight = highlight;
		this.abstractProyecto = abstractProyecto;
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
	
	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public ArrayList<String> getHighlight() {
		return highlight;
	}

	public void setHighlight(ArrayList<String> highlight) {
		this.highlight = highlight;
	}
	
	public String getAbstractProyecto() {
		return abstractProyecto;
	}

	public void setAbstractProyecto(String abstractProyecto) {
		this.abstractProyecto = abstractProyecto;
	}
	
	public String getResumenBusqueda()
	{
		String resumen = "";
		
		if(this.highlight != null)
		{
			int i=0;
			Iterator<String> iterador = this.highlight.iterator();
			while(iterador.hasNext() && i < Constantes.CANTIDAD_DETALLES_BUSQUEDA)
			{
				String valor = iterador.next();
				resumen= resumen  + " ..... " +  valor.replaceAll("<em>", "<b>").replaceAll("</em>", "</b>");
				i++;
			}
		}
		else
		{
			int maximo = this.abstractProyecto.length() > Constantes.LARGO_MAXIMO_RESUMEN_BUSQUEDA 
					? Constantes.LARGO_MAXIMO_RESUMEN_BUSQUEDA : this.abstractProyecto.length();
			resumen = this.abstractProyecto.substring(0, maximo);
		}		
		resumen = resumen + " ..... ";
		
		return resumen;
	}	
}