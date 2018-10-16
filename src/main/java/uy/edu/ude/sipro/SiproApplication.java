package uy.edu.ude.sipro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("uy.edu.ude")
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})

public class SiproApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiproApplication.class, args);
	}
}
