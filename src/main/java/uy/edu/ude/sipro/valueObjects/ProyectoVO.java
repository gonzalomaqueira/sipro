package uy.edu.ude.sipro.valueObjects;

import uy.edu.ude.sipro.entidades.Enumerados;
import uy.edu.ude.sipro.entidades.Enumerados.CategoriaProyectoEnum;
import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;

/*************************************************************************

Value object de interacción con la capa gráfica correspondiente a un proyecto

**************************************************************************/
public class ProyectoVO implements Comparable
{
	private int id;

	private int anio;
	
	private String codigoUde;

	private String carrera;

	private int nota;
	
	private String titulo;
	
	private EstadoProyectoEnum estado;
	
	private CategoriaProyectoEnum categoria;
	
	public ProyectoVO(int id, int anio, String codigoUde, String carrera, int nota, String titulo, EstadoProyectoEnum estado, CategoriaProyectoEnum categoria)
	{
		super();
		this.id = id;
		this.anio = anio;
		this.codigoUde= codigoUde;
		this.carrera = carrera;
		this.nota = nota;
		this.titulo = titulo;
		this.estado = estado;
		this.categoria = categoria;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public String getCodigoUde() {
		return codigoUde;
	}

	public void setCodigoUde(String codigoUde) {
		this.codigoUde = codigoUde;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Enumerados.EstadoProyectoEnum getEstado() {
		return estado;
	}

	public void setEstado(Enumerados.EstadoProyectoEnum estado) {
		this.estado = estado;
	}
	
	public CategoriaProyectoEnum getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProyectoEnum categoria) {
		this.categoria = categoria;
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
		return this.getId() - ((ProyectoVO) comparado).getId();
	}
}
