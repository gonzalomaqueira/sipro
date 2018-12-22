package uy.edu.ude.sipro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*************************************************************************

Clase de inicio de ejecución de la aplicación Sipro

**************************************************************************/
@SpringBootApplication
@EnableAutoConfiguration
public class SiproApplication extends SpringBootServletInitializer
{
	public static final String APP_URL = "/";
	public static final String LOGIN_URL = "/login.html";
	public static final String LOGOUT_URL = "/login.html?logout";
	public static final String LOGIN_FAILURE_URL = "/login.html?error";
	public static final String LOGIN_PROCESSING_URL = "/login";
	
	@Autowired
	private Inicio inicio;

	public static void main(String[] args)
	{
		SpringApplication.run(SiproApplication.class, args);
	}
}
