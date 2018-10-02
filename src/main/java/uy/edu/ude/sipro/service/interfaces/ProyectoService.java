package uy.edu.ude.sipro.service.interfaces;

import java.util.ArrayList;
import java.util.List;

import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.valueObjects.DocenteVO;

public interface ProyectoService 
{
	void agregar(String nombre, String carrera, List<DocenteVO> correctores, int nota, String rutaArchivo);
	void modificar(int id, String nombre, int anio, String carrera, int nota, String rutaArchivo);
	public void modificar(int id, String nombre, int anio, String carrera, int nota, String resumen, ArrayList<String> alumnos, Docente tutor, List<Docente> correctores);
	void eliminar(int id);
    List<Proyecto> obtenerProyectos();
	Proyecto obtenerProyectoPorId(int idProyecto);
	
	List<Elemento> obtenerElementosProyecto(Proyecto proyecto, List<Elemento> listaElementos);
	String[] obtenerTextoOriginalProyecto(Proyecto proyecto);
	
	void procesarProyecto(int id);
}
