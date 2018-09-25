package uy.edu.ude.sipro.dao.interfaces;

import java.util.List;

import uy.edu.ude.sipro.entidades.Usuario;

public interface UsuarioDao 
{   
	void agregar(Usuario usuario);
	void modificar(Usuario usuario);
	void eliminar(Usuario usuario);
	List<Usuario> obtenerUsuarios();
	Usuario obtenerUsuarioPorId(int id);
	Usuario buscarUsuario(String usuario);
}