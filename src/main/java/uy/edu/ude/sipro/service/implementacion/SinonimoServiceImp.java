package uy.edu.ude.sipro.service.implementacion;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.service.interfaces.SinonimoService;
import uy.edu.ude.sipro.dao.interfaces.SinonimoDao;

/*************************************************************************

Clase que implementa la interface SinonimoService

**************************************************************************/
@Service
public class SinonimoServiceImp implements SinonimoService
{
	@Autowired
	private SinonimoDao sinonimoDao;
	
	@Transactional
	@Override
	public void agregar(String nombre, Elemento elemento)
	{
		sinonimoDao.agregar(new Sinonimo(nombre, elemento));
	}

	@Transactional
	@Override
	public void modificar(int id, String nombre)
	{
		Sinonimo sinonimo = this.obtenerSinonimoPorId(id);
		sinonimo.setNombre(nombre);
		sinonimoDao.modificar(sinonimo);
	}

	@Transactional
	@Override
	public void eliminar(int id)
	{
		Sinonimo sinonimo = this.obtenerSinonimoPorId(id);
		sinonimoDao.eliminar(sinonimo);
	}

	@Transactional(readOnly = true)
	@Override
	public Set<Sinonimo> obtenerSinonimos()
	{
		return sinonimoDao.obtenerSinonimos();
	}
	
	@Transactional(readOnly = true)
	@Override
	public Sinonimo obtenerSinonimoPorId(int id)
	{
		return sinonimoDao.obtenerSinonimoPorId(id);
	}
}
