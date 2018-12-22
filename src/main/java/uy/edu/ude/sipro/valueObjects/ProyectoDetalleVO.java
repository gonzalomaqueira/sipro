package uy.edu.ude.sipro.valueObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uy.edu.ude.sipro.entidades.Enumerados.CategoriaProyectoEnum;
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.utiles.FuncionesTexto;

/*************************************************************************

Value object de interacción con la capa gráfica correspondiente al detalle de proyectos

**************************************************************************/
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
	
	private CategoriaProyectoEnum categoria;
	
	public ProyectoDetalleVO(int id, String codigoUde, int anio, String titulo, String carrera, List<DocenteVO> correctores, int nota, ArrayList<String> alumnos, 
							 DocenteVO tutor, ArrayList<String> tutorString, String rutaArchivo, String resumen, ArrayList<String> bibliografia, Date fechaAlta, 
							 Date fechaUltimaModificacion, List<ElementoVO> elementosRelacionados, CategoriaProyectoEnum categoria) 
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
		this.categoria = categoria;
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

	public CategoriaProyectoEnum getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProyectoEnum categoria) {
		this.categoria = categoria;
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
	
	public String getStringTutorString()
	{
		return FuncionesTexto.convertirArrayAStringSaltoLinea(this.getTutorString());
	}
	
	public String getStringAlumnos()
	{
		return FuncionesTexto.convertirArrayAStringSaltoLinea(this.getAlumnos());
	}
	
	public String getTecnologias()
	{
		String retorno="";
		for(ElementoVO e : this.getElementosRelacionados())
		{
			if(e.getTipoElemento() == TipoElemento.TECNOLOGIA)
				retorno= retorno + e.getNombre()+ "\n";
		}
		return retorno;
	}
	
	public String getMetodologiaTesting()
	{
		String retorno="";
		for(ElementoVO e : this.getElementosRelacionados())
		{
			if(e.getTipoElemento() == TipoElemento.METODOLOGIA_TESTING)
				retorno= retorno + e.getNombre()+ "\n";
		}
		return retorno;
	}
	
	public String getModeloProceso()
	{
		String retorno="";
		for(ElementoVO e : this.getElementosRelacionados())
		{
			if(e.getTipoElemento() == TipoElemento.MODELO_PROCESO)
				retorno= retorno + e.getNombre() + "\n";
		}
		return retorno;
	}
	
	public String getStringBibliografia()
	{
		return FuncionesTexto.convertirArrayAStringSaltoLinea(this.getBibliografia());
	}
	
	public String getStringCorrectores()
	{
		String retorno="";
		for(DocenteVO corrector : this.getCorrectores())
		{
			retorno= retorno + corrector.getNombreCompleto()+ "\n";
		}
		return retorno;
	}

	@Override
	public int compareTo(Object comparado)
	{
		return this.getId() - ((ProyectoDetalleVO) comparado).getId();
	}

}
