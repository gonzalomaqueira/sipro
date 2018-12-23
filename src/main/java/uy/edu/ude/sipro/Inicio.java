package uy.edu.ude.sipro;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.utiles.Constantes;

/*************************************************************************

Clase invocada una única vez cuando se inicia la aplicación

**************************************************************************/
@Component
public class Inicio
{
	@Autowired
	private Fachada fachada;
 
	@Autowired
	private Constantes constantes;
	
	@PostConstruct
	public void init()
	{
		try
		{
			fachada.creacionIncideES();
			fachada.sincronizacionDatosInicial();
			fachada.crearPerfilesInicio();
			fachada.crearUsuariosInicio();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}