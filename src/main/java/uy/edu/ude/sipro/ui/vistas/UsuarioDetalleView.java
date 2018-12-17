package uy.edu.ude.sipro.ui.vistas;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

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

import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.valueObjects.PerfilVO;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

@SpringView
@SpringComponent
@Secured("Administrador")
public class UsuarioDetalleView extends UsuarioDetalleViewDesign implements View
{
	@Autowired
	private Fachada fachada;
	
	private Set<PerfilVO> listaPerfiles;
	private UsuarioVO usuarioSeleccionado;
	private Binder<UsuarioVO> binder;
	private final NavigationManager navigationManager;
	private boolean esModoEdicion;
	
    @Autowired
    public UsuarioDetalleView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    	esModoEdicion = false;
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
			esModoEdicion = true;
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
		
		btnCancelar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				navigationManager.navigateTo(UsuarioDetalleView.class, idProyecto);
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
			    			if (usuarioSeleccionado.getContrasenia().isEmpty())
			    			{
				    			fachada.modificarUsuario(	usuarioSeleccionado.getId(),
				    										usuarioSeleccionado.getUsuario(),
				    										usuarioSeleccionado.getNombre(),
				    										usuarioSeleccionado.getApellido(),
				    										usuarioSeleccionado.getEmail(),
				    										usuarioSeleccionado.getPerfil());
			    			}
			    			else
			    			{
				    			fachada.modificarUsuario(	usuarioSeleccionado.getId(),
															usuarioSeleccionado.getUsuario(),
															usuarioSeleccionado.getContrasenia(),
															usuarioSeleccionado.getNombre(),
															usuarioSeleccionado.getApellido(),
															usuarioSeleccionado.getEmail(),
															usuarioSeleccionado.getPerfil());
			    			}
	    		
			    			UIUtiles.mostrarNotificacion("USUARIO", "Modificación exitosa", Notification.Type.HUMANIZED_MESSAGE);			    			
			    			navigationManager.navigateTo(UsuarioListadoView.class);
			    		}
			    		else
			    		{
			    			fachada.altaUsuario(	usuarioSeleccionado.getUsuario(),
													usuarioSeleccionado.getContrasenia(),
													usuarioSeleccionado.getNombre(),
													usuarioSeleccionado.getApellido(),
													usuarioSeleccionado.getEmail(),
													usuarioSeleccionado.getPerfil());
			    		
			    			UIUtiles.mostrarNotificacion("USUARIO", "Alta exitosa", Notification.Type.HUMANIZED_MESSAGE);			    			
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
		    		UIUtiles.mostrarNotificacion("ERROR", "Ocurrió algún problema con Alta usuario", Notification.Type.ERROR_MESSAGE);	
		    		navigationManager.navigateTo(UsuarioListadoView.class);
				}
		    }
		});
		
	}
	
	private void SetearBinder()
	{
		binder = new Binder<UsuarioVO>(UsuarioVO.class);
		
		binder.forField(txtUsuario)
			.withValidator(usuario -> usuario.length() >= 2, "Usuario debe tener al menos 2 caracteres")
			.withValidator(usuario -> (esModoEdicion || !fachada.existeUsuario(usuario)), "Usuario ya existente")
			.bind(UsuarioVO::getUsuario, UsuarioVO::setUsuario);
		
        binder.forField(txtNombre)
        	.withValidator(nombre -> nombre.length() >= 2, "Nombre debe tener al menos 2 caracteres")
        	.bind(UsuarioVO::getNombre, UsuarioVO::setNombre);
        
        binder.forField(txtApellido)
        	.withValidator(a -> a.length() >= 2, "Apellido debe tener al menos 2 caracteres")
        	.bind(UsuarioVO::getApellido, UsuarioVO::setApellido);

        binder.forField(txtEmail)
        	.withValidator(new EmailValidator("El formato del email no es válido "))
        	.bind(UsuarioVO::getEmail, UsuarioVO::setEmail);
        
        binder.forField(txtContrasenia)
        	.withValidator(c -> c.length() >= 5 || (esModoEdicion && c.length() == 0), "La contraseña debe contener al menos 5 caracteres")
        	.bind(UsuarioVO::getContrasenia, UsuarioVO::setContrasenia);

        binder.forField(txtRepetirContasenia)
        	.withValidator(c -> c.equals(txtContrasenia.getValue()), "Las contraseñas no coinciden")
        	.bind(UsuarioVO::getRepetirContrasenia, UsuarioVO::setRepetirContrasenia);
        
        binder.forField(cmbPerfil)
        	.withValidator(c -> c != null, "Debe seleccionar un perfil")
        	.bind(UsuarioVO::getPerfil, UsuarioVO::setPerfil);
        
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
