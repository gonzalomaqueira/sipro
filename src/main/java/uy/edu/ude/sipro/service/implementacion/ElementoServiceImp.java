package uy.edu.ude.sipro.service.implementacion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.dao.interfaces.ElementoDao;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.service.interfaces.SinonimoService;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;

@Service
public class ElementoServiceImp implements ElementoService 
{
	@Autowired
	private ElementoDao elementoDao;
	
	@Autowired
	private SinonimoService sinonimoService;

	@Transactional
	@Override
	public void agregar(Elemento elemento)
	{
		elementoDao.agregar(elemento);
	}
	
	@Transactional
	@Override
	public void altaElemento(String nombre, boolean esCategoria, TipoElemento tipoElemento, Set<SubElementoVO> elementosRelacionados, Set<SinonimoVO> sinonimos)
	{
		Set<Elemento> listaRelaciones = new HashSet<Elemento>();
		Set<Sinonimo> listaSinonimos= new HashSet<Sinonimo>();
		Elemento elemento = new Elemento(nombre, esCategoria, tipoElemento, listaRelaciones, listaSinonimos);
		elementoDao.agregar(elemento);
		
		Set<Elemento> todosElementos= elementoDao.obtenerElementos();
		
		for(Elemento elem : todosElementos)
		{
			if(elemento.getNombre().equals(elem.getNombre()) )
			{
				elemento= elem;
			}
		}
		
		for(SinonimoVO sin : sinonimos)
		{
			sinonimoService.agregar(sin.getNombre(), elemento);
		}
		
		listaSinonimos=sinonimoService.obtenerSinonimos();
		Set<Sinonimo> listaAux= new HashSet<Sinonimo>(listaSinonimos);
		for(Sinonimo sin : listaAux)
		{
			if(sin.getElemento().getId()!=elemento.getId())
			{
				listaSinonimos.remove(sin);
			}
		}
	
		if(todosElementos != null)
		{
			for(Elemento elem : todosElementos)
			{
				for(SubElementoVO subElem : elementosRelacionados)
				{
					if( subElem.getId()==elem.getId())
					{
						listaRelaciones.add(elem);
						elem.getElementosOrigen().add(elemento);						
					}
				}
			 }
			elemento.setSinonimos(listaSinonimos);
			elemento.setElementosRelacionados(listaRelaciones);
			elementoDao.agregar(elemento);
		}
	}

	@Transactional
	@Override
	public void modificar(int id, String nombre, boolean esCategoria, TipoElemento tipoElemento,
						  Set<SubElementoVO> elementosRelacionados, Set<SinonimoVO> sinonimos)
	{
		Elemento elemento = this.obtenerElementoPorId(id);
		elemento.setNombre(nombre);
		elemento.setEsCategoria(esCategoria);
		elemento.setTipoElemento(tipoElemento);
		
		Set<Sinonimo> listaSinonimos= new HashSet<Sinonimo>();
		Set<Elemento> listaEliminar = new HashSet<Elemento>();
		Set<Elemento> listaTotal= elementoDao.obtenerElementos();
		Set<Elemento> listaGuardar = new HashSet<Elemento>();
		
		boolean existe;
		

		for( Elemento elem  : elemento.getElementosRelacionados())
		{
			existe=false;
			for(SubElementoVO subElem : elementosRelacionados)
			{
				if(subElem.getId() == elem.getId())
				{
					existe=true;
					break;
				}
			}
			
			if(!existe)
				listaEliminar.add(elem);
		}
		
		for(Elemento elem : listaTotal)
		{
			for(SubElementoVO subElem : elementosRelacionados)
			{
				if(elem.getId() == subElem.getId())
				{					
					listaGuardar.add(elem);
					elem.getElementosOrigen().add(elemento);
				}
			}
			
			for(Elemento elem2 : listaEliminar)
			{
				if(elem.getId() == elem2.getId())
				{
					elem.getElementosOrigen().remove(elemento);
				}
			}
		}
		
		
		listaSinonimos=sinonimoService.obtenerSinonimos();
		Set<Sinonimo> listaAux= new HashSet<Sinonimo>(listaSinonimos);
		for(Sinonimo sin : listaAux)
		{
			if(sin.getElemento().getId()!=elemento.getId())
			{
				listaSinonimos.remove(sin);
			}
		}
		
		// Se agregan nuevos sinonimos
		listaAux= new HashSet<Sinonimo>(listaSinonimos);
		for(SinonimoVO sinVO : sinonimos)
		{
			existe=false;
			for(Sinonimo sin : listaAux)
			{
				if(sinVO.getId()==sin.getId() || sinVO.getNombre().toLowerCase().equals(sin.getNombre().toLowerCase()))
				{
					existe=true;
					break;
				}
			}
			if (!existe)
				sinonimoService.agregar(sinVO.getNombre(), elemento);		
		}
		
		//Se eliminan sinonimos desasociados
		for(Sinonimo sin : listaSinonimos )
		{
			existe=false;
			for(SinonimoVO sinVO : sinonimos)
			{
				if(sinVO.getId()==sin.getId() || sinVO.getNombre().toLowerCase().equals(sin.getNombre().toLowerCase()))
				{
					existe=true;
					break;
				}
			}
			if (!existe)
			{
				elemento.getSinonimos().remove(sin);
				sinonimoService.eliminar(sin.getId());
			}
		}

		listaSinonimos=sinonimoService.obtenerSinonimos();
		listaAux= new HashSet<Sinonimo>(listaSinonimos);
		for(Sinonimo sin : listaAux)
		{
			if(sin.getElemento().getId()!=elemento.getId())
			{
				listaSinonimos.remove(sin);
			}
		}
		
		elemento.setElementosRelacionados(listaGuardar);
		elemento.setSinonimos(listaSinonimos);
		elementoDao.modificar(elemento);
	}

	@Transactional
	@Override
	public void eliminar(int id)
	{
		Elemento elemento = this.obtenerElementoPorId(id);
		for (Proyecto proy: elemento.getProyectos())
	    {
		    proy.getElementosRelacionados().remove(elemento);
	    }
	    elemento.getProyectos().removeAll(elemento.getProyectos());
/////////////////////////////////////////////////////////////////////////////
	    
		for(Elemento eR : elemento.getElementosRelacionados())
		{		
			for (Elemento eO : eR.getElementosOrigen())
			{
				if(eO.getId() == elemento.getId())
				{
					eR.getElementosOrigen().remove(elemento);
					break;
				}
			}
		}
		elemento.getElementosRelacionados().removeAll(elemento.getElementosRelacionados());
		
		
		for(Elemento eO : elemento.getElementosOrigen())
		{
			for (Elemento eR : eO.getElementosRelacionados())
			{
				if(eR.getId() == elemento.getId())
				{
					eO.getElementosRelacionados().remove(elemento);
					break;
				}
			}
		}
		elemento.getElementosOrigen().removeAll(elemento.getElementosOrigen());
		
////////////////////////////////////////////////////////////////////////
	    elementoDao.eliminar(elemento);
	}
	
	@Transactional
	@Override
	public void eliminar(Elemento elemento)
	{
		elementoDao.eliminar(elemento);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Set<Elemento> obtenerElementos()
	{
		return elementoDao.obtenerElementos();
	}
		
	@Transactional(readOnly = true)
	@Override
	public Elemento obtenerElementoPorId(int id)
	{
		return elementoDao.obtenerElementoPorId(id);	
	}
}
