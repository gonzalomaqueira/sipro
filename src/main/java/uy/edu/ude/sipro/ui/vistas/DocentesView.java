package uy.edu.ude.sipro.ui.vistas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

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
import uy.edu.ude.sipro.valueObjects.DocenteVO;

@SpringView
@SpringComponent
@Scope("prototype")
public class DocentesView extends DocentesViewDesign implements View {
	
	@Autowired
	private Fachada fachada;
	private DocenteVO docenteSeleccionado;
	
    private final NavigationManager navigationManager;
    
    @Autowired
    public DocentesView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		cargarInterfazInicial();
		
		btnAgregar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				if(!txtNombreDocente.isEmpty() && !txtApellidoDocente.isEmpty())
				{
					fachada.altaDocente(txtNombreDocente.getValue(), txtApellidoDocente.getValue());
					cargarInterfazInicial();
				}
				else
					Notification.show("Hay valores vacíos", Notification.Type.WARNING_MESSAGE);
			}
		});
		
		btnCancelar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				navigationManager.navigateTo(DocentesView.class);
			}
		});
		
		btnBorrar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				try
				{
					fachada.eliminarDocente(docenteSeleccionado.getId());
					Notification.show("Docente eliminado correctamente", Notification.Type.WARNING_MESSAGE);
					cargarInterfazInicial();
				}
				catch (Exception e)
				{
					Notification.show("Error al eliminar docente", Notification.Type.WARNING_MESSAGE);
				}
			}
		});
		
		grdDocentes.addSelectionListener(evt -> 
		{
			SingleSelectionModel<DocenteVO> singleSelect = (SingleSelectionModel<DocenteVO>) grdDocentes.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					docenteSeleccionado = singleSelect.getSelectedItem().get();
					txtNombreDocente.setValue(docenteSeleccionado.getNombre());
					txtApellidoDocente.setValue(docenteSeleccionado.getApellido());
					btnAgregar.setEnabled(false);
					btnBorrar.setVisible(true);
					btnEditar.setVisible(true);
				}
			}
			catch (Exception e)
			{
			}
		});
	
		
	}
	
	private void cargarInterfazInicial()
	{
		grdDocentes.setItems(fachada.obtenerDocentes());
		btnBorrar.setVisible(false);
		btnEditar.setVisible(false);
	}
	
}
