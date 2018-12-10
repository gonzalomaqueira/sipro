package uy.edu.ude.sipro.service.interfaces;


import java.util.ArrayList;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.valueObjects.DocenteVO;

public interface ProyectoService 
{
	void agregar(String codigoUde, String carrera, Set<DocenteVO> correctoresVO, int nota, String rutaArchivo);
	void modificar(int id, String codigoUde, int anio, String carrera, int nota, String rutaArchivo);
	void modificar(int id, String codigoUde, String titulo, int anio, String carrera, int nota, String resumen, 
			ArrayList<String> alumnos, ArrayList<String> tutorString, Set<Docente> correctores, ArrayList<String> bibliografia) throws Exception;
	
	void modificar(Proyecto proyecto);
	
	void eliminar(int id) throws Exception;
    Set<Proyecto> obtenerProyectos();
	Proyecto obtenerProyectoPorId(int idProyecto);
	
	Proyecto buscarProyecto (String codigoUde);
	
	Set<Elemento> obtenerElementosProyecto(Proyecto proyecto, Set<Elemento> listaElementos);
	String[] obtenerTextoOriginalProyecto(Proyecto proyecto);	
	void cargarTutorPorString(Proyecto proyecto);
	
	void procesarProyecto(int id) throws Exception;
	void cargarDatosProyectoES(Proyecto proyecto) throws Exception;
}
