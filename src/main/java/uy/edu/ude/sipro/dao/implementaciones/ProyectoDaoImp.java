package uy.edu.ude.sipro.dao.implementaciones;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.dao.interfaces.ProyectoDao;

@Repository
public class ProyectoDaoImp implements ProyectoDao
{
	@PersistenceContext
	private EntityManager em;

	@Override
	public void agregar(Proyecto proyecto)
	{
		em.merge(proyecto);
    }

	@Override
	public void modificar(Proyecto proyecto)
	{
		em.merge(proyecto);
    }

	@Override
	public void eliminar(Proyecto proyecto)
	{
		em.remove(em.contains(proyecto) ? proyecto : em.merge(proyecto));
    }

	@Override
	public Set<Proyecto> obtenerProyectos()
	{
	      CriteriaQuery<Proyecto> criteriaQuery = em.getCriteriaBuilder().createQuery(Proyecto.class);
	      @SuppressWarnings("unused")
	      Root<Proyecto> root = criteriaQuery.from(Proyecto.class);
	      return new HashSet<Proyecto>(em.createQuery(criteriaQuery).getResultList());
	}
	
	@Override
	public Proyecto obtenerProyectoPorId(int id)
	{
		return (Proyecto)em.find(Proyecto.class, id);
	}
	
	@Override
	public Proyecto buscarProyecto(String codigoUde)
	{
		Proyecto proy;
		try
		{
			TypedQuery<Proyecto> query = em.createQuery("SELECT u FROM Proyecto u WHERE u.codigoUde = :codigoUde", Proyecto.class);
			query.setParameter("codigoUde", codigoUde);
			proy = query.getSingleResult();
		}			
		catch (Exception e)
		{
			proy = null;
		}
		return proy;
	} 
}
