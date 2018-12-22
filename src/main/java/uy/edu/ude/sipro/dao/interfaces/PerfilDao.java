package uy.edu.ude.sipro.dao.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Perfil;

/*************************************************************************

Interface encargada de la persistencia de Perfiles

**************************************************************************/
public interface PerfilDao
{
	void agregar(Perfil perfil);
	Set<Perfil> obtenerPerfiles();
}
