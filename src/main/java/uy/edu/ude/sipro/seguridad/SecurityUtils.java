package uy.edu.ude.sipro.seguridad;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.service.interfaces.UsuarioService;

/*************************************************************************

 Utilitario de funciones estáticas para SrpingSecurity

**************************************************************************/
public class SecurityUtils {

	private SecurityUtils()
	{
	}

	public static String getUsername() 
	{
		SecurityContext context = SecurityContextHolder.getContext();
		UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
		return userDetails.getUsername();
	}

	public static boolean isCurrentUserInRole(String role) 
	{
		return getUserRoles().stream().filter(roleName -> roleName.equals(Objects.requireNonNull(role))).findAny()
				.isPresent();
	}

	public static Set<String> getUserRoles() 
	{
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}

	public static Usuario getCurrentUser(UsuarioService usuarioService) 
	{
		return usuarioService.buscarUsuario(SecurityUtils.getUsername());
	}
}
