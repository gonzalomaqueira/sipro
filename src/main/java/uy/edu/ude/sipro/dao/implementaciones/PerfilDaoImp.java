package uy.edu.ude.sipro.dao.implementaciones;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.dao.interfaces.PerfilDao;

/*************************************************************************

Implementación de la interface PerfilDao

**************************************************************************/
@Repository
public class PerfilDaoImp implements PerfilDao
{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void agregar(Perfil perfil)
	{
		em.merge(perfil);
	}
	
	@Override
	public Set<Perfil> obtenerPerfiles() 
	{
		 CriteriaQuery<Perfil> criteriaQuery = em.getCriteriaBuilder().createQuery(Perfil.class);
		 @SuppressWarnings("unused")
		 Root<Perfil> root = criteriaQuery.from(Perfil.class);
		 return new HashSet<Perfil>(em.createQuery(criteriaQuery).getResultList());
	}
}
