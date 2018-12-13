package uy.edu.ude.sipro;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uy.edu.ude.sipro.service.Fachada;

@Component
public class Inicio {
	
	@Autowired
	private Fachada fachada;
 
	@PostConstruct
	public void init()
	{
		try
		{
			fachada.creacionIncideES();
			fachada.sincronizacionDatosInicial();
			fachada.crearPerfileInicio();
			fachada.crearUsuariosInicio();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}