package uy.edu.ude.sipro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("uy.edu.ude")
public class SiproApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiproApplication.class, args);
	}
}
