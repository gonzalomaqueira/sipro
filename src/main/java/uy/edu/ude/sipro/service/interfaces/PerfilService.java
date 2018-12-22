package uy.edu.ude.sipro.service.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Perfil;

/*************************************************************************

Interface encargada de la lógica de negocio para Perfiles

**************************************************************************/
public interface PerfilService
{
	void agregar(int id, String contenido);
    Set<Perfil> obtenerPerfiles();
    String[] obtenerPerfilesString();
}
