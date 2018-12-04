package uy.edu.ude.sipro.ui.vistas;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.seguridad.SecurityUtils;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.service.interfaces.UsuarioService;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

@SpringView
@SpringComponent
public class PerfilView extends PerfilViewDesign implements View{
	
	@Autowired
	private Fachada fachada;
	private UsuarioVO usuario;
	
	public void enter(ViewChangeEvent event) 
	{	
		cssContasenias.setVisible(false);
		usuario = fachada.obtenerUsuarioLogeado();
		cargarDatosUsuario();
		
		chkContrasenias.addValueChangeListener(e ->
		{
			
			if(chkContrasenias.getValue())	
				cssContasenias.setVisible(true);
			else
				cssContasenias.setVisible(false);
		}
		);
	}
	
	private void cargarDatosUsuario()
	{
		txtNombre.setValue(usuario.getNombre());
		txtApellido.setValue(usuario.getApellido());
		txtEmail.setValue(usuario.getEmail());
	}
	
}

