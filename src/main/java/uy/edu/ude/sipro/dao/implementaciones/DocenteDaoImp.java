package uy.edu.ude.sipro.dao.implementaciones;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import uy.edu.ude.sipro.dao.interfaces.DocenteDao;
import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Sinonimo;

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
	public List<Docente> obtenerDocentes()
	{
		CriteriaQuery<Docente> criteriaQuery = em.getCriteriaBuilder().createQuery(Docente.class);
		@SuppressWarnings("unused")
		Root<Docente> root = criteriaQuery.from(Docente.class);
		return em.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public Docente obtenerDocentePorId(int id)
	{
		Session session = em.unwrap(Session.class);
		Object instancia = session.find(Sinonimo.class, id);
    	return (Docente)instancia;
	}
}
