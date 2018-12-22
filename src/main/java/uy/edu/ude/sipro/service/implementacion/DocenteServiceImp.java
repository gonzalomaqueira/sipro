package uy.edu.ude.sipro.service.implementacion;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uy.edu.ude.sipro.dao.interfaces.DocenteDao;
import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.service.interfaces.DocenteService;

/*************************************************************************

Clase que implementa la interface DocenteService

**************************************************************************/
@Service
public class DocenteServiceImp implements DocenteService
{
	@Autowired
	private DocenteDao docenteDao;
	
	@Transactional
	@Override
	public void agregar(String nombre, String apellido)
	{
		docenteDao.agregar(new Docente(nombre, apellido));
	}
	
	@Transactional
	@Override
	public void modificar(int id, String nombre, String apellido)
	{
		Docente docente = this.obtenerDocentePorId(id);
		docente.setNombre(nombre);
		docente.setNombre(apellido);
		docenteDao.modificar(docente);
	}
	
	@Transactional
	@Override
	public void eliminar(int id)
	{
		Docente docente = this.obtenerDocentePorId(id);
		
		for (Proyecto proy: docente.getProyectosComoCorrector())
	    {
		    proy.getCorrectores().remove(docente);
	    }
		
		docente.getProyectosComoCorrector().removeAll(docente.getProyectosComoCorrector());
		
		for (Proyecto proy: docente.getProyectosComoTutor())
	    {
		    proy.setTutor(null);
	    }
		
		docenteDao.eliminar(docente);
	}
    
	@Transactional(readOnly = true)
	@Override
	public Set<Docente> obtenerDocentes()
	{
		return docenteDao.obtenerDocentes();
	}
	
	@Transactional(readOnly = true)
	@Override
    public Docente obtenerDocentePorId(int id)
    {
    	return docenteDao.obtenerDocentePorId(id);
    }
	
	@Transactional(readOnly = true)
	@Override
	public boolean existeDocente(String nombre, String apellido)
	{
		return docenteDao.existeDocente(nombre, apellido);
	}
}
