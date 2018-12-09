package uy.edu.ude.sipro.ui.vistas;

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

import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.seguridad.SecurityUtils;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.service.interfaces.UsuarioService;
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

@SpringView
@SpringComponent
public class PerfilView extends PerfilViewDesign implements View{
	
	@Autowired
	private Fachada fachada;
	private UsuarioVO usuario;
	private Binder<UsuarioVO> binder;
	private boolean editContrasenia;
	private boolean esModoEdicion;
	private final NavigationManager navigationManager;
	
    @Autowired
    public PerfilView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
	
	
	public void enter(ViewChangeEvent event) 
	{	
		editContrasenia=false;
		esModoEdicion=false;
		modoEditable(false);
		cssContasenias.setVisible(false);
		usuario = fachada.obtenerUsuarioLogeado();
		cargarDatosUsuario();
		this.SetearBinder();
		
		chkContrasenias.addValueChangeListener(e ->
		{
			
			if(chkContrasenias.getValue())
			{
				cssContasenias.setVisible(true);
				editContrasenia= true;
			}	
			else	
			{
				cssContasenias.setVisible(false);
				editContrasenia=false;
			}
		}
		);
		
		btnEditar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event) 
			{
				modoEditable(true);
			}
		});
		
		btnCancelar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event) 
			{
				cargarDatosUsuario();
				txtContrasenia.clear();
				txtContraseniaActual.clear();
				txtRepetirContasenia.clear();
				modoEditable(false);
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
                        binder.writeBean(usuario);
                        
			    		if(usuario.getId() > 0)
			    		{

			    			fachada.modificarUsuario(	usuario.getId(),
								    					usuario.getUsuario(),
								    					usuario.getContrasenia(),
								    					usuario.getNombre(),
								    					usuario.getApellido(),
								    					usuario.getEmail(),
								    					usuario.getPerfil());
			    		
	    		
			    			UIUtiles.mostrarNotificacion("USUARIO", "Modificación exitosa", Notification.Type.HUMANIZED_MESSAGE);			    			
			    			navigationManager.navigateTo(PerfilView.class);
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
		    		navigationManager.navigateTo(PerfilView.class);
				}
		    }
		});
	}
	
	private void cargarDatosUsuario()
	{
		txtNombre.setValue(usuario.getNombre());
		txtApellido.setValue(usuario.getApellido());
		txtEmail.setValue(usuario.getEmail());
	}
	
	private void modoEditable(boolean modo)
	{
		if(modo)
		{
			esModoEdicion= true;
			btnCancelar.setVisible(true);
			btnEditar.setVisible(false);
			btnGuardar.setVisible(true);
			txtNombre.setEnabled(true);
			txtApellido.setEnabled(true);
			txtEmail.setEnabled(true);
			txtContrasenia.setEnabled(true);
			txtContraseniaActual.setEnabled(true);
			txtRepetirContasenia.setEnabled(true);
		}
		else
		{
			esModoEdicion= false;
			btnCancelar.setVisible(false);
			btnEditar.setVisible(true);
			btnGuardar.setVisible(false);
			txtNombre.setEnabled(false);
			txtApellido.setEnabled(false);
			txtEmail.setEnabled(false);
			txtContrasenia.setEnabled(false);
			txtContraseniaActual.setEnabled(false);
			txtRepetirContasenia.setEnabled(false);
		}
	}
	
	private void SetearBinder()
	{
		binder = new Binder<UsuarioVO>(UsuarioVO.class);
		
		
        binder.forField(txtNombre)
        	.withValidator(nombre -> nombre.length() >= 2, "Nombre debe tener al menos 2 caracteres")
        	.bind(UsuarioVO::getNombre, UsuarioVO::setNombre);
        
        binder.forField(txtApellido)
        	.withValidator(a -> a.length() >= 2, "Apellido debe tener al menos 2 caracteres")
        	.bind(UsuarioVO::getApellido, UsuarioVO::setApellido);

        binder.forField(txtEmail)
        	.withValidator(new EmailValidator("El formato del email no es válido "))
        	.bind(UsuarioVO::getEmail, UsuarioVO::setEmail);
        
        /*
        binder.forField(txtContrasenia)
        	.withValidator(c -> c.length() >= 5 && (esModoEdicion && editContrasenia), "La contraseña debe contener al menos 5 caracteres")
        	.bind(UsuarioVO::getContrasenia, UsuarioVO::setContrasenia);

        binder.forField(txtRepetirContasenia)
        	.withValidator(c -> c.equals(txtContrasenia.getValue()) && (esModoEdicion && editContrasenia), "Las contraseñas no coinciden")
        	.bind(UsuarioVO::getRepetirContrasenia, UsuarioVO::setRepetirContrasenia);
        
        binder.forField(txtContraseniaActual)
    	.withValidator(c -> c.equals(usuario.getContrasenia()) && (esModoEdicion && editContrasenia), "Las contraseñas actual no es correcta")
    	.bind(UsuarioVO::getContraseniaActual, UsuarioVO::setContraseniaActual);
        */
        
        binder.readBean(usuario);
	}
	
}

