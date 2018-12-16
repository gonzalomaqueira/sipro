package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.valueObjects.DocenteVO;

@SpringView
@SpringComponent
@Scope("prototype")
@Secured("Admin")
public class DocentesView extends DocentesViewDesign implements View {
	
	@Autowired
	private Fachada fachada;
	private Binder<DocenteVO> binder;
	private DocenteVO docenteSeleccionado;
	private List<DocenteVO> listaDocentes;
	
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
		docenteSeleccionado= new DocenteVO();
		this.SetearBinder();				
		
		txtBuscar.addValueChangeListener(evt -> 
		{
		    filtrarLista(evt.getValue());
		});
		
		btnNuevo.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				cssApellido.setVisible(true);
				cssNombre.setVisible(true);
				btnCancelar.setVisible(true);
				btnAgregar.setVisible(true);
				btnBorrar.setVisible(false);
				btnNuevo.setVisible(false);
				
			}
		});
		
		btnAgregar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				try 
		    	{
					BinderValidationStatus<DocenteVO> statusValidacion = binder.validate();
		    		if (statusValidacion.isOk())
		    		{
	                    binder.writeBean(docenteSeleccionado);
	                    if(!fachada.existeDocente(docenteSeleccionado.getNombre(),docenteSeleccionado.getApellido()))
	                    {
							fachada.altaDocente(docenteSeleccionado.getNombre(), docenteSeleccionado.getApellido());
							UIUtiles.mostrarNotificacion("DOCENTE", "Alta exitosa", Notification.Type.HUMANIZED_MESSAGE);
							navigationManager.navigateTo(DocentesView.class);
	                    }
	                    else
	                    {
	                    	UIUtiles.mostrarNotificacion("DOCENTE", "Ya existente", Notification.Type.ERROR_MESSAGE);
	                    }
					}
		    		else
		    		{
		    			UIUtiles.mostrarErrorValidaciones(statusValidacion.getValidationErrors());
		    		}
					
		    	}
				catch (Exception e)
				{
		    		UIUtiles.mostrarNotificacion("ERROR", "Ocurrió algún problema con Alta docente", Notification.Type.ERROR_MESSAGE);	
		    		navigationManager.navigateTo(DocentesView.class);
				}
	    		
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
				if (docenteSeleccionado != null)
				{
					ConfirmDialog.show(UI.getCurrent(), "Confirmación:", "¿Seguro que desea eliminar?",
					        "Eliminar", "Cancelar", new ConfirmDialog.Listener() {

					            public void onClose(ConfirmDialog dialog)
					            {
					                if (dialog.isConfirmed())
					                {
					                	try
					    				{
					    					fachada.eliminarDocente(docenteSeleccionado.getId());
					    					UIUtiles.mostrarNotificacion("DOCENTE", "Baja exitosa", Notification.Type.HUMANIZED_MESSAGE);
					    					cargarInterfazInicial();
					    				}
					    				catch (Exception e)
					    				{
					    					UIUtiles.mostrarNotificacion("ERROR", "Ocurrió algún problema con Baja docente", Notification.Type.ERROR_MESSAGE);
					    				}		    					
					                } else {
					                }
					            }
					        });				
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
					btnCancelar.setVisible(true);
					btnAgregar.setVisible(false);
					btnBorrar.setVisible(true);
					btnNuevo.setVisible(true);
					cssApellido.setVisible(false);
					cssNombre.setVisible(false);
				}
			}
			catch (Exception e)
			{
			}
		});
	
		
	}

	private void cargarInterfazInicial()
	{
		this.listaDocentes = fachada.obtenerDocentes();
		txtNombreDocente.clear();
		txtApellidoDocente.clear();
		grdDocentes.setItems(this.listaDocentes);
		btnCancelar.setVisible(false);
		btnAgregar.setVisible(false);
		btnBorrar.setVisible(false);
		btnNuevo.setVisible(true);
		cssApellido.setVisible(false);
		cssNombre.setVisible(false);
	}
	
	private void SetearBinder()
	{
		binder = new Binder<DocenteVO>(DocenteVO.class);
		
		binder.forField(txtNombreDocente)
			.withValidator(nombre -> nombre.length() >= 3, "Nombre debe tener al menos 3 caracteres")
			.bind(DocenteVO::getNombre, DocenteVO::setNombre);
		
        binder.forField(txtApellidoDocente)
        	.withValidator(apellido -> apellido.length() >= 3, "Apellido debe tener al menos 3 caracteres")
        	.bind(DocenteVO::getApellido, DocenteVO::setApellido);
         
        binder.readBean(docenteSeleccionado);
	}
	
	private void filtrarLista(String texto) 
	{
		List<DocenteVO> listaAux = new ArrayList<>();
		for (DocenteVO docente : this.listaDocentes)
		{
			if (docente.getNombreCompleto().toLowerCase().contains(texto.toLowerCase().trim()))
			{
				listaAux.add(docente);
			}
		}
		grdDocentes.setItems(listaAux);
	}
}
