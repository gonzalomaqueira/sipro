package uy.edu.ude.sipro.busquedas;

import uy.edu.ude.sipro.valueObjects.DocenteVO;

public class DatosFiltro {
	
	private String tutor;
	private int anioIni;
	private int anioFin;
	private int notaIni;
	private int notaFin;
	private boolean filtroHabilitado;
	
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
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
	public boolean isFiltroHabilitado() {
		return filtroHabilitado;
	}
	public void setFiltroHabilitado(boolean filtroHabilitado) {
		this.filtroHabilitado = filtroHabilitado;
	}

	

}