package uy.edu.ude.sipro.service.interfaces;

import java.util.List;

import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.entidades.Usuario;

public interface UsuarioService
{
	void agregar(String nombreUsuario, String contrasenia, String nombre, String apellido, String email, Perfil perfil);
	void modificar(int id, String nombreUsuario, String contrasenia, String nombre, String apellido, String email, Perfil perfil);
	void eliminar(int id);
    List<Usuario> obtenerUsuarios();
	Usuario buscarUsuario(String user);
}