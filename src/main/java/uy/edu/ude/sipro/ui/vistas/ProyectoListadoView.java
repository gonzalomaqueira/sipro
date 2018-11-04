package uy.edu.ude.sipro.ui.vistas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.valueObjects.ProyectoVO;

@SpringView
@SpringComponent
public class ProyectoListadoView extends ProyectoListadoViewDesign implements View{
	
	@Autowired
	private Fachada fachada;
	
	private ProyectoVO proyectoSeleccionado;
	private List<ProyectoVO> listaProyectos;
	
	private final NavigationManager navigationManager;
	
    @Autowired
    public ProyectoListadoView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
	
	public void enter(ViewChangeEvent event)
	{
		cargarInterfazInicial();
		
		grdProyectos.addSelectionListener(evt -> 
		{
			SingleSelectionModel<ProyectoVO> singleSelect = (SingleSelectionModel<ProyectoVO>) grdProyectos.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					proyectoSeleccionado = singleSelect.getSelectedItem().get();
					btnVerDetalles.setEnabled(true);
					btnProcesar.setEnabled(true);
					btnEliminar.setEnabled(true);
					
					if (proyectoSeleccionado.getEstado() == EstadoProyectoEnum.SIN_PROCESAR)
					{
						btnProcesar.setCaption("PROCESAR");
						btnVerDetalles.setEnabled(false);
					}
					else
					{
						btnProcesar.setCaption("RE-PROCESAR");
						btnVerDetalles.setEnabled(true);
					}
				}
			}
			catch (Exception e)
			{
			}
		});
		
		btnVerDetalles.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if(proyectoSeleccionado != null)
					navigationManager.navigateTo(ProyectoDetallesView.class , proyectoSeleccionado.getId());
			}
		});
		
		btnAgregar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				navigationManager.navigateTo(ProyectoNuevoView.class);
			}
		});
		
		btnEliminar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if( proyectoSeleccionado != null )
				{
					fachada.borrarProyecto(proyectoSeleccionado.getId());
					UIUtiles.mostrarNotificacion("PROYECTO", "Baja exitosa", Notification.Type.HUMANIZED_MESSAGE);
					cargarInterfazInicial();
				}
			}
		});
		
		btnProcesar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if( proyectoSeleccionado != null )
				{
					fachada.ProcesarProyecto(proyectoSeleccionado.getId());
					navigationManager.navigateTo(ProyectoDetallesView.class, proyectoSeleccionado.getId());
					UIUtiles.mostrarNotificacion("PROYECTO", "Procesado exitosamente", Notification.Type.HUMANIZED_MESSAGE);
				}
			}
		});
	}

	private void cargarInterfazInicial()
	{
		btnProcesar.setEnabled(false);
		btnVerDetalles.setEnabled(false);
		btnEliminar.setEnabled(false);
		listaProyectos = fachada.obtenerProyectos();
		grdProyectos.setItems(listaProyectos);
		actualizarProyectoSeleccionado();
	}
	
	private void actualizarProyectoSeleccionado() 
	{
		if (listaProyectos != null && proyectoSeleccionado != null)
		{
			for(ProyectoVO proy : listaProyectos)
			{
				if (proy.getId() == proyectoSeleccionado.getId())
				{
					proyectoSeleccionado = proy;
				}
			}
		}
	}
}
