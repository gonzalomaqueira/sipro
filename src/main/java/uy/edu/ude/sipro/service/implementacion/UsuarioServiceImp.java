package uy.edu.ude.sipro.service.implementacion;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.service.interfaces.UsuarioService;
import uy.edu.ude.sipro.dao.interfaces.UsuarioDao;
import uy.edu.ude.sipro.utiles.FuncionesTexto;

@Service
public class UsuarioServiceImp implements UsuarioService 
{
   @Autowired
   private UsuarioDao usuarioDao;
   
	@Autowired
	private PasswordEncoder passwordEncoder;
   
   @Transactional
   @Override
   public void agregar(String nombreUsuario, String contrasenia, String nombre, String apellido, String email, Perfil perfil)
   {
	   Usuario usuario = new Usuario(nombreUsuario, passwordEncoder.encode(contrasenia), nombre, apellido, email, perfil);
	   usuarioDao.agregar(usuario);
   }
   
   @Transactional
   @Override
   public void modificar(int id, String nombreUsuario, String contrasenia, String nombre, String apellido, String email, Perfil perfil)
   {
	   if(FuncionesTexto.esNuloOVacio(contrasenia))
	   {
		  contrasenia=buscarUsuario(nombreUsuario).getContrasenia();
	   }
	   else
	   {
		   passwordEncoder.encode(contrasenia);
	   }
	   
	   Usuario usuario = new Usuario(nombreUsuario, passwordEncoder.encode(contrasenia), nombre, apellido, email, perfil);
	   usuario.setId(id);
	   usuarioDao.modificar(usuario);
   }   
   
   @Transactional
   @Override
   public void modificarSinContrasenia(int id, String nombreUsuario, String nombre, String apellido, String email, Perfil perfil)
   {	   
	   Usuario usuario = usuarioDao.obtenerUsuarioPorId(id);
	   usuario.setUsuario(nombreUsuario);
	   usuario.setNombre(nombre);
	   usuario.setApellido(apellido);
	   usuario.setEmail(email);
	   usuario.setPerfil(perfil);
	   usuarioDao.modificar(usuario);
   }
   
   @Transactional
   @Override
   public void eliminar(int id)
   {
	   Usuario usuario = new Usuario();
	   usuario.setId(id);
	   usuarioDao.eliminar(usuario);
   }
   
   @Transactional(readOnly = true)
   @Override
   public Set<Usuario> obtenerUsuarios()
   {
      return usuarioDao.obtenerUsuarios();
   }
   
   @Transactional
   @Override
   public Usuario buscarUsuario(String usuario)
   {
	   return usuarioDao.buscarUsuario(usuario);
   }
   
   @Transactional
   @Override
   public Usuario buscarUsuarioPorId(int id)
   {
	   return usuarioDao.obtenerUsuarioPorId(id);
   }
   
}