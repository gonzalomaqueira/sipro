package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.validator.EmailValidator;
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
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.valueObjects.PerfilVO;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

@SpringView
@SpringComponent
public class UsuarioDetalleView extends UsuarioDetalleViewDesign implements View
{
	@Autowired
	private Fachada fachada;
	
	private Set<PerfilVO> listaPerfiles;
	private UsuarioVO usuarioSeleccionado;
	private Binder<UsuarioVO> binder;
	private final NavigationManager navigationManager;
	
    @Autowired
    public UsuarioDetalleView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
    
	public void enter(ViewChangeEvent event) 
	{
		cargarPerfiles(); 
		
		String idProyecto = event.getParameters();
		if ("".equals(idProyecto))
		{
			cargarVistaNuevoUsuario();
		}
		else
		{
			usuarioSeleccionado=fachada.obtenerUsuarioPorId(Integer.parseInt(idProyecto));
			cargarVistaModificarUsuario(Integer.parseInt(idProyecto));
		}
		
		if (usuarioSeleccionado == null)
		{
			usuarioSeleccionado = new UsuarioVO();
		}
		
		this.SetearBinder();
		
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
		    	try 
		    	{
		    		BinderValidationStatus<UsuarioVO> statusValidacion = binder.validate();
		    		if (statusValidacion.isOk())
		    		{
                        binder.writeBean(usuarioSeleccionado);
                        
			    		if(usuarioSeleccionado.getId() > 0)
			    		{
			    			fachada.modificarUsuario(	usuarioSeleccionado.getId(),
			    										usuarioSeleccionado.getUsuario(),
			    										usuarioSeleccionado.getContrasenia(),
			    										usuarioSeleccionado.getNombre(),
			    										usuarioSeleccionado.getApellido(),
			    										usuarioSeleccionado.getEmail(),
						    							cmbPerfil.getSelectedItem().get());	
	    		
			    			Notification.show("Usuario modificado correctamente",Notification.Type.WARNING_MESSAGE);
			    		}
			    		else
			    		{
			    			fachada.altaUsuario(	usuarioSeleccionado.getUsuario(),
													usuarioSeleccionado.getContrasenia(),
													usuarioSeleccionado.getNombre(),
													usuarioSeleccionado.getApellido(),
													usuarioSeleccionado.getEmail(),
									    			cmbPerfil.getSelectedItem().get());	
			    		
			    			Notification.show("Usuario ingresado correctamente",Notification.Type.WARNING_MESSAGE);
			    			
			    			navigationManager.navigateTo(UsuarioListadoView.class);
			    		}
			    	}
		    		else
		    		{
		    			UIUtiles.mostrarErrorValidaciones(statusValidacion.getValidationErrors());
		    		}
		    			
		    	}
		    	catch (Exception e)
				{
		    		Notification.show("Error al ingresar usuario",Notification.Type.WARNING_MESSAGE);
		    		navigationManager.navigateTo(UsuarioListadoView.class);
				}
		    }
		});
		
	}
	
	private void SetearBinder()
	{
		binder = new Binder<UsuarioVO>(UsuarioVO.class);
		
		binder.forField(txtUsuario)
			.withValidator(nombre -> nombre.length() >= 2, "Debe tener al menos 2 caracteres")
			.bind(UsuarioVO::getUsuario, UsuarioVO::setUsuario);
		
        binder.forField(txtNombre)
        	.withValidator(nombre -> nombre.length() >= 2, "Debe tener al menos 2 caracteres")
        	.bind(UsuarioVO::getNombre, UsuarioVO::setNombre);
        
        binder.forField(txtApellido)
        	.withValidator(a -> a.length() >= 2, "Debe tener al menos 2 caracteres")
        	.bind(UsuarioVO::getApellido, UsuarioVO::setApellido);

        binder.forField(txtEmail)
        	.withValidator(new EmailValidator("El formato no es válido "))
        	.bind(UsuarioVO::getEmail, UsuarioVO::setEmail);
        
        binder.forField(txtContrasenia)
        	.withValidator(c -> c.length() >= 5, "La contraseña debe contener al menos 5 caracteres")
        	.bind(UsuarioVO::getContrasenia, UsuarioVO::setContrasenia);

        binder.forField(txtRepetirContasenia)
        	.withValidator(c -> c.equals(txtContrasenia.getValue()), "Las contraseñas no coinciden")
        	.bind(UsuarioVO::getRepetirContrasenia, UsuarioVO::setRepetirContrasenia);
        
        binder.readBean(usuarioSeleccionado);
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
	
	private void cargarVistaNuevoUsuario()
	{
		txtUsuario.setReadOnly(false);
	}
	
	private void cargarVistaModificarUsuario(int idProyecto)
	{
		txtUsuario.setValue(usuarioSeleccionado.getUsuario());
		txtNombre.setValue(usuarioSeleccionado.getNombre());
		txtApellido.setValue(usuarioSeleccionado.getApellido());
		txtEmail.setValue(usuarioSeleccionado.getEmail());
		cmbPerfil.setValue(usuarioSeleccionado.getPerfil());
	}
}
