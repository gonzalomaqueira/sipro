package uy.edu.ude.sipro.dao.implementaciones;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.dao.interfaces.SinonimoDao;

@Repository
public class SinonimoDaoImp implements SinonimoDao
{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void agregar(Sinonimo sinonimo) 
	{
		em.merge(sinonimo);
	}

	@Override
	public void modificar(Sinonimo sinonimo) 
	{
		em.merge(sinonimo);
	}

	@Override
	public void eliminar(Sinonimo sinonimo)
	{
		em.remove(em.contains(sinonimo) ? sinonimo : em.merge(sinonimo));
	}

	@Override
	public List<Sinonimo> obtenerSinonimos()
	{
		CriteriaQuery<Sinonimo> criteriaQuery = em.getCriteriaBuilder().createQuery(Sinonimo.class);
		@SuppressWarnings("unused")
		Root<Sinonimo> root = criteriaQuery.from(Sinonimo.class);
		return em.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public Sinonimo obtenerSinonimoPorId(int id)
	{
		Session session = em.unwrap(Session.class);
		Object instancia = session.find(Sinonimo.class, id);
    	return (Sinonimo)instancia;
	}

}
