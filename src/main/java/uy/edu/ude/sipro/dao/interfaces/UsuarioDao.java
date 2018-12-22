package uy.edu.ude.sipro.dao.interfaces;

import java.util.Set;

import uy.edu.ude.sipro.entidades.Usuario;

/*************************************************************************

Interface encargada de la persistencia de Docentes

**************************************************************************/
public interface UsuarioDao 
{   
	void agregar(Usuario usuario);
	void modificar(Usuario usuario);
	void eliminar(Usuario usuario);
	Set<Usuario> obtenerUsuarios();
	Usuario obtenerUsuarioPorId(int id);
	Usuario buscarUsuario(String usuario);
}