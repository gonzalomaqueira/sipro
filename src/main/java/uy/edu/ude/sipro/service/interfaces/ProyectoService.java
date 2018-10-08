package uy.edu.ude.sipro.service.interfaces;


import java.util.ArrayList;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.valueObjects.DocenteVO;

public interface ProyectoService 
{
	void agregar(String nombre, String carrera, Set<DocenteVO> correctores, int nota, String rutaArchivo);
	void modificar(int id, String nombre, int anio, String carrera, int nota, String rutaArchivo);
	public void modificar(int id, String nombre, int anio, String carrera, int nota, String resumen, ArrayList<String> alumnos, Docente tutor, Set<Docente> correctores);
	void eliminar(int id);
    Set<Proyecto> obtenerProyectos();
	Proyecto obtenerProyectoPorId(int idProyecto);
	
	Set<Elemento> obtenerElementosProyecto(Proyecto proyecto, Set<Elemento> listaElementos);
	String[] obtenerTextoOriginalProyecto(Proyecto proyecto);	
	void cargarTutorPorString(Proyecto proyecto);
	
	void procesarProyecto(int id);
}
