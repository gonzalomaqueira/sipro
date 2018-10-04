package uy.edu.ude.sipro.valueObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProyectoDetalleVO
{
	private int id;

	private String nombre;

	private int anio;

	private String carrera;
	
	private List<DocenteVO> correctores;

	private int nota;
	
	private ArrayList<String> alumnos;
	
	private DocenteVO tutor;
	
	private ArrayList<String> tutorString;
	
	private String resumen;
	
	private String rutaArchivo;
	
	private Date fechaAlta;
	
	private Date fechaUltimaModificacion;

	private List <ElementoVO> elementosRelacionados;
	
	public ProyectoDetalleVO(int id, String nombre, int anio, String carrera, List<DocenteVO> correctores, int nota, ArrayList<String> alumnos, 
							 DocenteVO tutor, ArrayList<String> tutorString, String rutaArchivo, String resumen, Date fechaAlta, 
							 Date fechaUltimaModificacion, List<ElementoVO> elementosRelacionados) 
	{
		this.id = id;
		this.nombre = nombre;
		this.anio = anio;
		this.carrera = carrera;
		this.correctores = correctores;
		this.nota = nota;
		this.alumnos = alumnos;
		this.tutor = tutor;
		this.tutorString = tutorString;
		this.rutaArchivo = rutaArchivo;
		this.resumen = resumen;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
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
	
	public List<DocenteVO> getCorrector() {
		return correctores;
	}

	public void setCorrector(List<DocenteVO> correctores) {
		this.correctores = correctores;
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
	
	
}
