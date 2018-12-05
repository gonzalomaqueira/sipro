package uy.edu.ude.sipro.valueObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProyectoDetalleVO implements Comparable
{
	private int id;

	private String codigoUde;

	private int anio;
	
	private String titulo;

	private String carrera;
	
	private List<DocenteVO> correctores;

	private int nota;
	
	private ArrayList<String> alumnos;
	
	private DocenteVO tutor;
	
	private ArrayList<String> tutorString;
	
	private String resumen;
	
	private ArrayList<String> bibliografia;
	
	private String rutaArchivo;
	
	private Date fechaAlta;
	
	private Date fechaUltimaModificacion;

	private List <ElementoVO> elementosRelacionados;
	
	public ProyectoDetalleVO(int id, String codigoUde, int anio, String titulo, String carrera, List<DocenteVO> correctores, int nota, ArrayList<String> alumnos, 
							 DocenteVO tutor, ArrayList<String> tutorString, String rutaArchivo, String resumen, ArrayList<String> bibliografia, Date fechaAlta, 
							 Date fechaUltimaModificacion, List<ElementoVO> elementosRelacionados) 
	{
		this.id = id;
		this.codigoUde = codigoUde;
		this.anio = anio;
		this.titulo = titulo;
		this.carrera = carrera;
		this.correctores = correctores;
		this.nota = nota;
		this.alumnos = alumnos;
		this.tutor = tutor;
		this.tutorString = tutorString;
		this.rutaArchivo = rutaArchivo;
		this.resumen = resumen;
		this.bibliografia = bibliografia;
		this.fechaAlta = fechaAlta;
		this.fechaUltimaModificacion = fechaUltimaModificacion;
		this.elementosRelacionados = elementosRelacionados;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCarrera() {
		return carrera;
	}

	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}
	
	public ArrayList<String> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(ArrayList<String> alumnos) {
		this.alumnos = alumnos;
	}

	public DocenteVO getTutor() {
		return tutor;
	}

	public void setTutor(DocenteVO tutor) {
		this.tutor = tutor;
	}
	
	public List<DocenteVO> getCorrectores() {
		return correctores;
	}

	public void setCorrectores(List<DocenteVO> correctores) {
		this.correctores = correctores;
	}

	public ArrayList<String> getTutorString() {
		return tutorString;
	}

	public void setTutorString(ArrayList<String> tutorString) {
		this.tutorString = tutorString;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaUltimaModificacion() {
		return fechaUltimaModificacion;
	}

	public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
		this.fechaUltimaModificacion = fechaUltimaModificacion;
	}

	public List<ElementoVO> getElementosRelacionados() {
		return elementosRelacionados;
	}

	public void setElementosRelacionados(List<ElementoVO> elementosRelacionados) {
		this.elementosRelacionados = elementosRelacionados;
	}
	
	public ArrayList<String> getBibliografia() {
		return bibliografia;
	}

	public void setBibliografia(ArrayList<String> bibliografia) {
		this.bibliografia = bibliografia;
	}

	public String getAnioString() 
	{
		return Integer.toString(this.anio);
	}

	public void setAnioString(String anio) 
	{
		
		try
		{
			this.anio =Integer.parseInt(anio);
		}
		catch(Exception e)
		{
			
		}
		
	}

	public String getNotaString() 
	{
		return Integer.toString(this.nota);
	}

	public void setNotaString(String nota) 
	{
		try
		{
			this.nota =Integer.parseInt(nota);
		}
		catch(Exception e)
		{
			
		}
	}
	

	@Override
	public int compareTo(Object comparado)
	{
		return this.getId() - ((ProyectoDetalleVO) comparado).getId();
	}

}
