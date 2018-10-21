package uy.edu.ude.sipro.service.implementacion;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.service.interfaces.PerfilService;
import uy.edu.ude.sipro.dao.interfaces.PerfilDao;

@Service
public class PerfilServiceImp implements PerfilService
{
	@Autowired
	private PerfilDao perfilDao;
	
	@Transactional(readOnly = true)
	@Override
	public Set<Perfil> obtenerPerfiles()
	{
		return perfilDao.obtenerPerfiles();
	}	
    
	@Override
	public String[] obtenerPerfilesString()
	{
				
		String[] retorno= new String[20];
		Set<Perfil> listaPerfiles = this.obtenerPerfiles();
		if (listaPerfiles != null && !listaPerfiles.isEmpty())
		{
			
			int i = 0;
			for (Perfil per : listaPerfiles)
			{
				retorno[i] = per.getDescripcion();
				i++;
			}
		}
		return retorno;
	}
}
