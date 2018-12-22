package uy.edu.ude.sipro.seguridad;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.service.interfaces.UsuarioService;

/*************************************************************************

Clase utilizada para el logueo a través de SpringSecurity

**************************************************************************/
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{	
	private final UsuarioService usuarioService;

	@Autowired
	public UserDetailsServiceImpl(UsuarioService usuarioService) 
	{
		this.usuarioService = usuarioService;
	}

	@Override
	public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException 
	{
		Usuario usuario = usuarioService.buscarUsuario(nombreUsuario);
		if (null == usuario) {
			throw new UsernameNotFoundException("No user present with username: " + nombreUsuario);
		} else {
			return new org.springframework.security.core.userdetails.User(usuario.getUsuario(), usuario.getContrasenia(),
					Collections.singletonList(new SimpleGrantedAuthority(usuario.getPerfil().getDescripcion())));
		}
	}
}