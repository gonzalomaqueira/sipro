package uy.edu.ude.sipro;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.vaadin.ui.Notification;

import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.ui.UIUtiles;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
//@EnableAutoConfiguration
public class SiproApplication {
	
	public static final String APP_URL = "/";
	public static final String LOGIN_URL = "/login.html";
	public static final String LOGOUT_URL = "/login.html?logout";
	public static final String LOGIN_FAILURE_URL = "/login.html?error";
	public static final String LOGIN_PROCESSING_URL = "/login";
	
	@Autowired
	private Inicio inicio;

	public static void main(String[] args) {
		SpringApplication.run(SiproApplication.class, args);
	}
		
		

	
	
}
