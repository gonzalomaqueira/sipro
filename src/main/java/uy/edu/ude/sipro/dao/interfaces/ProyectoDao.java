package uy.edu.ude.sipro.dao.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Proyecto;

/*************************************************************************

Interface encargada de la persistencia de Proyectos

**************************************************************************/
public interface ProyectoDao
{
	void agregar(Proyecto proyecto);
	void modificar(Proyecto proyecto);
	void eliminar(Proyecto proyecto);
	Set<Proyecto> obtenerProyectos();
	Proyecto obtenerProyectoPorId(int id);
	Proyecto buscarProyecto(String codigoUde);
}
