package uy.edu.ude.sipro.busquedas;

import java.util.ArrayList;
import java.util.Iterator;

public class ResultadoBusqueda
{
	private int idProyecto;
	private String tituloProyecto;
	private float score;
	private String codigoUde;
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
		while(iterador.hasNext() && i <= 3)
		{
			String valor = iterador.next();
			resumen= resumen  +" ... " +  valor;
			i++;
		}
		
		return resumen;
	}
	

}