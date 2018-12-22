package uy.edu.ude.sipro.service.interfaces;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.entidades.Usuario;

/*************************************************************************

Interface encargada de la lógica de negocio para Usuarios

**************************************************************************/
public interface UsuarioService
{
	void agregar(String nombreUsuario, String contrasenia, String nombre, String apellido, String email, Perfil perfil);
	void modificar(int id, String nombreUsuario, String contrasenia, String nombre, String apellido, String email, Perfil perfil);
	void eliminar(int id);
	void modificarSinContrasenia(int id, String nombreUsuario, String nombre, String apellido, String email, Perfil perfil);
    Set<Usuario> obtenerUsuarios();
	Usuario buscarUsuario(String user);
	Usuario buscarUsuarioPorId(int id);
}