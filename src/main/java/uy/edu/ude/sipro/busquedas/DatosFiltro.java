package uy.edu.ude.sipro.busquedas;

import java.util.List;

import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;

public class DatosFiltro {
	
	private String tutorString;
	private String correctorString;
	private String bibliografia;
	private int anioIni;
	private int anioFin;
	private int notaIni;
	private int notaFin;
	
	private DocenteVO tutorObjeto;
	private DocenteVO CorrectorObjeto;
	private List<ElementoVO> listaElementos;
	private boolean filtroHabilitado;
	
	public String getTutorString() {
		return tutorString;
	}
	public void setTutorString(String tutor) {
		this.tutorString = tutor;
	}
	public String getCorrectorString() {
		return correctorString;
	}
	public void setCorrectorString(String correctorString) {
		this.correctorString = correctorString;
	}
	public int getAnioIni() {
		return anioIni;
	}
	public void setAnioIni(int anioIni) {
		this.anioIni = anioIni;
	}
	public int getAnioFin() {
		return anioFin;
	}
	public void setAnioFin(int anioFin) {
		this.anioFin = anioFin;
	}
	public int getNotaIni() {
		return notaIni;
	}
	public void setNotaIni(int notaIni) {
		this.notaIni = notaIni;
	}
	public int getNotaFin() {
		return notaFin;
	}
	public void setNotaFin(int notaFin) {
		this.notaFin = notaFin;
	}	
	public List<ElementoVO> getListaElementos() {
		return listaElementos;
	}
	public void setListaElementos(List<ElementoVO> listaElementos) {
		this.listaElementos = listaElementos;
	}
	public boolean isFiltroHabilitado() {
		return filtroHabilitado;
	}
	public void setFiltroHabilitado(boolean filtroHabilitado) {
		this.filtroHabilitado = filtroHabilitado;
	}

	public String getBibliografia() {
		return bibliografia;
	}
	public void setBibliografia(String bibliografia) {
		this.bibliografia = bibliografia;
	}
	public String getStringRangoAnios()
	{
		return this.getAnioIni() + " - " + this.getAnioFin();
	}
	
	public String getStringRangoNotas()
	{
		return this.getNotaIni() + " - " + this.getNotaFin();
	}	
	
	public DocenteVO getTutorObjeto() {
		return tutorObjeto;
	}
	public void setTutorObjeto(DocenteVO tutorObjeto) {
		this.tutorObjeto = tutorObjeto;
	}
	public DocenteVO getCorrectorObjeto() {
		return CorrectorObjeto;
	}
	public void setCorrectorObjeto(DocenteVO correctorObjeto) {
		CorrectorObjeto = correctorObjeto;
	}
	
	public String getStringListaElementos()
	{
		String retorno = "";
		if (this.getListaElementos() != null && !this.getListaElementos().isEmpty())
		{
			for(ElementoVO elemento : this.getListaElementos())
			{
				retorno = retorno + elemento.getNombre() + ", ";
			}
			retorno = retorno.substring(0, retorno.length()-2);
		}
		return retorno;
	}
}
