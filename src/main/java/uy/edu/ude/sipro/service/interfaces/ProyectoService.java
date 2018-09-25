package uy.edu.ude.sipro.service.interfaces;

import java.util.ArrayList;
import java.util.List;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.utiles.SeccionTexto;

public interface ProyectoService 
{
	void agregar(String nombre, int anio, String carrera, int nota, String rutaArchivo);
	void modificar(int id, String nombre, int anio, String carrera, int nota, String rutaArchivo);
	void modificar(int id, String nombre, int anio, String carrera, int nota, String resumen, ArrayList<String> alumnos, ArrayList<String> tutor);
	void eliminar(int id);
    List<Proyecto> obtenerProyectos();
	Proyecto obtenerProyectoPorId(int idProyecto);
	
	List<Elemento> obtenerElementosProyecto(Proyecto proyecto, List<Elemento> listaElementos);
	String[] obtenerTextoOriginalProyecto(Proyecto proyecto);
	List<SeccionTexto> armarDocumentoPorSecciones(String textoOriginal[]);				
	SeccionTexto armarSeccionAlumnos(String texto[]);
	SeccionTexto armarSeccionTutor(String texto[]);
	
	void procesarProyecto(int id);
}
