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

	private int nota;
	
	private ArrayList<String> alumnos;
	
	private ArrayList<String> tutor;
	
	private String resumen;
	
	private String rutaArchivo;
	
	private Date fechaAlta;
	
	private Date fechaUltimaModificacion;

	private List <ElementoVO> elementosRelacionados;
	
	public ProyectoDetalleVO(int id, String nombre, int anio, String carrera, int nota, ArrayList<String> alumnos, 
							 ArrayList<String> tutor, String rutaArchivo, String resumen, Date fechaAlta, 
							 Date fechaUltimaModificacion, List<ElementoVO> elementosRelacionados) 
	{
		this.id = id;
		this.nombre = nombre;
		this.anio = anio;
		this.carrera = carrera;
		this.nota = nota;
		this.alumnos = alumnos;
		this.tutor = tutor;
		this.rutaArchivo = rutaArchivo;
		this.resumen = resumen;
		this.fechaAlta = fechaAlta;
		this.fechaUltimaModificacion = fechaUltimaModificacion;
		this.elementosRelacionados = elementosRelacionados;
	}
	
	
}
