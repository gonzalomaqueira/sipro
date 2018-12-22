package uy.edu.ude.sipro.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import uy.edu.ude.sipro.SiproApplication;
import uy.edu.ude.sipro.service.interfaces.PerfilService;

/*************************************************************************

Clase encargada de la configuración de seguridad de SpringSecurity

**************************************************************************/
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private PerfilService perfilService;
	
	private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	private final RedirectAuthenticationSuccessHandler successHandler;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
			RedirectAuthenticationSuccessHandler successHandler)
	{
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.successHandler = successHandler;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http.csrf().disable();

		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry reg = http
				.authorizeRequests();

		reg = reg.antMatchers("/VAADIN/**").permitAll();
		reg = reg.antMatchers("/**").hasAnyAuthority(perfilService.obtenerPerfilesString());
		HttpSecurity sec = reg.and();

		FormLoginConfigurer<HttpSecurity> login = sec.formLogin().permitAll();
		login = login.loginPage(SiproApplication.LOGIN_URL).loginProcessingUrl(SiproApplication.LOGIN_PROCESSING_URL)
				.failureUrl(SiproApplication.LOGIN_FAILURE_URL).successHandler(successHandler);
		login.and().logout().logoutSuccessUrl(SiproApplication.LOGOUT_URL);
	}
}
