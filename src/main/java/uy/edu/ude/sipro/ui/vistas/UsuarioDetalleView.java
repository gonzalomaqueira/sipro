package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;

import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.valueObjects.PerfilVO;

@SpringView
@SpringComponent
public class UsuarioDetalleView extends UsuarioDetalleViewDesign implements View
{
	
	@Autowired
	private Fachada fachada;
	
	private Set<PerfilVO> listaPerfiles;
	
	private final NavigationManager navigationManager;
	
    @Autowired
    public UsuarioDetalleView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
    
	public void enter(ViewChangeEvent event) 
	{
		cargarPerfiles(); 
		
		btnVolver.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				navigationManager.navigateTo(UsuarioListadoView.class);
			}
		});
		
		btnGuardar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event) 
			{
				if (txtUsuario.isEmpty() || txtNombre.isEmpty() || txtApellido.isEmpty() || txtEmail.isEmpty() || txtContrasenia.isEmpty() 
						|| txtRepetirContasenia.isEmpty() || cmbPerfil.isEmpty() ) 
				{
					Notification.show("Hay valores vacíos",Notification.Type.WARNING_MESSAGE);
				}
				else 
				{	
					if (!(txtContrasenia.getValue().equals((txtRepetirContasenia.getValue())))) 
					{
							Notification.show("Las contraseñas no coinciden",Notification.Type.WARNING_MESSAGE);
					}
					else 
					{		    		
				    	try 
				    	{
				    		fachada.altaUsuario(txtUsuario.getValue(),
				    							txtContrasenia.getValue(),
				    							txtNombre.getValue(),
				    							txtApellido.getValue(),
				    							txtEmail.getValue(),
				    							cmbPerfil.getSelectedItem().get());	
				    		
				    		Notification.show("Usuario ingresado correctamente",Notification.Type.WARNING_MESSAGE);
				    	}
				    	catch (Exception e)
						{
				    		Notification.show("Error al ingresar usuario",Notification.Type.WARNING_MESSAGE);				
						}
				    	
				    	navigationManager.navigateTo(UsuarioListadoView.class);

				    }
				}	
			}
		});
		
	}
	
	private void cargarPerfiles() 
	{
		if (listaPerfiles == null)
		{
			listaPerfiles = new HashSet<PerfilVO>();
			listaPerfiles.addAll(fachada.obtenerPerfiles());
			cmbPerfil.setItems(listaPerfiles);
			cmbPerfil.setItemCaptionGenerator(PerfilVO::getDescripcion);
		}
	}
}
