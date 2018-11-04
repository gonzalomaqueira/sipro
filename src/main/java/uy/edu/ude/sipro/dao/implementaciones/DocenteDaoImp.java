package uy.edu.ude.sipro.dao.implementaciones;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import uy.edu.ude.sipro.dao.interfaces.DocenteDao;
import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.entidades.Usuario;

@Repository
public class DocenteDaoImp implements DocenteDao
{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void agregar(Docente docente)
	{
		em.merge(docente);
	}

	@Override
	public void modificar(Docente docente)
	{
		em.merge(docente);
	}

	@Override
	public void eliminar(Docente docente) 
	{
		em.remove(em.contains(docente) ? docente : em.merge(docente));
	}

	@Override
	public Set<Docente> obtenerDocentes()
	{
		CriteriaQuery<Docente> criteriaQuery = em.getCriteriaBuilder().createQuery(Docente.class);
		@SuppressWarnings("unused")
		Root<Docente> root = criteriaQuery.from(Docente.class);
		return new HashSet<Docente>(em.createQuery(criteriaQuery).getResultList());
	}

	@Override
	public Docente obtenerDocentePorId(int id)
	{
		return (Docente)em.find(Docente.class, id);
	}
	
	@Override
	public boolean existeDocente(String nombre, String apellido)
	{
		Docente doc;
		try
		{
			TypedQuery<Docente> query = em.createQuery("SELECT u FROM Docente u WHERE u.nombre = :nombre and u.apellido= :apellido", Docente.class);
			query.setParameter("nombre", nombre);
			query.setParameter("apellido", apellido);
			doc = query.getSingleResult();
		}			
		catch (Exception e)
		{
			doc = null;
		}
		return doc!=null;
	}
}
