package uy.edu.ude.sipro.valueObjects;

import uy.edu.ude.sipro.entidades.Enumerados;
import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;

public class ProyectoVO implements Comparable
{
	private int id;

	private String nombre;

	private int anio;

	private String carrera;

	private int nota;
	
	private EstadoProyectoEnum estado;
	
	public ProyectoVO(int id, String nombre, int anio, String carrera, int nota, EstadoProyectoEnum estado)
	{
		super();
		this.id = id;
		this.nombre = nombre;
		this.anio = anio;
		this.carrera = carrera;
		this.nota = nota;
		this.estado = estado;
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

	public Enumerados.EstadoProyectoEnum getEstado() {
		return estado;
	}

	public void setEstado(Enumerados.EstadoProyectoEnum estado) {
		this.estado = estado;
	}

	@Override
	public int compareTo(Object comparado)
	{
		return this.getId() - ((ProyectoVO) comparado).getId();
	}
}
