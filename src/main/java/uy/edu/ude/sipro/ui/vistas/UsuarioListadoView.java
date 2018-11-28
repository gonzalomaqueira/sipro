package uy.edu.ude.sipro.ui.vistas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.components.grid.SingleSelectionModel;


import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

@SpringView
@SpringComponent
//@Secured("admin")
public class UsuarioListadoView extends UsuarioListadoViewDesign implements View
{
	@Autowired
	private Fachada fachada;
	
	private UsuarioVO usuarioSeleccionado;
	private List<UsuarioVO> listaUsuarios;
	
	
	private final NavigationManager navigationManager;
	
    @Autowired
    public UsuarioListadoView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
    
	public void enter(ViewChangeEvent event) 
	{
		grdUsuarios.addColumn(usuario -> usuario.getPerfil().getDescripcion()).setCaption("Perfil");
		cargarInterfazInicial();
		
		btnAgregar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				navigationManager.navigateTo(UsuarioDetalleView.class);
			}
		});
		
		btnBorrar.addClickListener(new Button.ClickListener() {
		    public void buttonClick(ClickEvent event) {
		    	
		    	try 
		    	{
		    		fachada.eliminarUsuario(usuarioSeleccionado.getId()); 	
			    	cargarInterfazInicial();
			    	UIUtiles.mostrarNotificacion("USUARIO", "Baja exitosa", Notification.Type.HUMANIZED_MESSAGE);
		    	}
		    	catch (Exception e)
				{
		    		UIUtiles.mostrarNotificacion("ERROR", "Ocurrió algún problema con baja usuario", Notification.Type.ERROR_MESSAGE);		
				}			
		    }
		});	
		
		btnEditar.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event)
			{			
				if(usuarioSeleccionado != null)
					navigationManager.navigateTo(UsuarioDetalleView.class , usuarioSeleccionado.getId());
			}
		});
		
		grdUsuarios.addSelectionListener(evt -> 
		{
			SingleSelectionModel<UsuarioVO> singleSelect = (SingleSelectionModel<UsuarioVO>) grdUsuarios.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					usuarioSeleccionado = singleSelect.getSelectedItem().get();
					btnEditar.setEnabled(true);
					btnBorrar.setEnabled(true);
				}
			}
			catch (Exception e)
			{
			}
		});
	}
	
	private void cargarInterfazInicial()
	{
		btnBorrar.setEnabled(false);
		btnEditar.setEnabled(false);
		btnAgregar.setEnabled(true);
		listaUsuarios = fachada.obtenerUsuarios();
		grdUsuarios.setItems(listaUsuarios);
	}
}
