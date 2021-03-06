package uy.edu.ude.sipro;

import com.vaadin.spring.access.SecuredViewAccessControl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*************************************************************************

Clase ApplicationConfiguration

**************************************************************************/
@Configuration
public class ApplicationConfiguration {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecuredViewAccessControl securedViewAccessControl()
	{
		return new SecuredViewAccessControl();
	}
}
