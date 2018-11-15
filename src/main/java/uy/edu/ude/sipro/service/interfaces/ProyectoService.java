package uy.edu.ude.sipro.service.interfaces;


import java.util.ArrayList;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.valueObjects.DocenteVO;

public interface ProyectoService 
{
	void agregar(String codigoUde, String nombre, String carrera, Set<DocenteVO> correctoresVO, int nota, String rutaArchivo);
	void modificar(int id, String codigoUde, String nombre, int anio, String carrera, int nota, String rutaArchivo);
	public void modificar(int id, String codigoUde, String nombre, String Titulo, int anio, String carrera, int nota, String resumen, ArrayList<String> alumnos, ArrayList<String> tutorString, Set<Docente> correctores);
	void eliminar(int id);
    Set<Proyecto> obtenerProyectos();
	Proyecto obtenerProyectoPorId(int idProyecto);
	
	Set<Elemento> obtenerElementosProyecto(Proyecto proyecto, Set<Elemento> listaElementos);
	String[] obtenerTextoOriginalProyecto(Proyecto proyecto);	
	void cargarTutorPorString(Proyecto proyecto);
	
	void procesarProyecto(int id) throws Exception;
	
	String buscarProyectoES(String keywords) throws Exception;	
	boolean altaProyectoES(Proyecto proyecto, String[] textoOriginal) throws Exception;
}
