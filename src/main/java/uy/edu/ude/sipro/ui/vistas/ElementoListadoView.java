package uy.edu.ude.sipro.ui.vistas;

import org.springframework.beans.factory.annotation.Autowired;

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
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;

@SpringView
@SpringComponent
public class ElementoListadoView extends ElementoListadoViewDesign implements View{
	
	
	@Autowired
    private Fachada fachada;
	
	private ElementoVO elementoSeleccionado;
	
	private final NavigationManager navigationManager;
	
    @Autowired
    public ElementoListadoView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
    
	
	public void enter(ViewChangeEvent event) 
	{
		
		cargarListaElementos();
		btnEliminar.setEnabled(false);
		
		grdElementos.addSelectionListener(evt -> 
		{
			SingleSelectionModel<ElementoVO> singleSelect = (SingleSelectionModel<ElementoVO>) grdElementos.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					elementoSeleccionado = singleSelect.getSelectedItem().get();
					btnEliminar.setEnabled(true);
					cargarListaRelaciones();
					cargarListaSinonimos();
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
				if(elementoSeleccionado != null)
					navigationManager.navigateTo(ElementoDetalleView.class , elementoSeleccionado.getId());
			}
		});
		
		btnAgregar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				navigationManager.navigateTo(ElementoDetalleView.class);
			}
		});
		
		btnEliminar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if( elementoSeleccionado!=null )
				{
					fachada.eliminarElemento(elementoSeleccionado.getId());
					Notification.show("Elemento eliminado exitosamente", Notification.Type.WARNING_MESSAGE);
					cargarListaElementos();
				}
			}
		});
		
	}
	
	private void cargarListaElementos()
	{
		this.grdElementos.setItems(fachada.obtenerElementos());
	}
	
	private void cargarListaRelaciones()
	{
		String texto="";
		for(SubElementoVO elemento : elementoSeleccionado.getElementosRelacionados())
		{
			texto= texto.concat(elemento.getNombre());
			texto= texto.concat("; ");
		}
		txtRelaciones.setValue(texto);
	}
	
	private void cargarListaSinonimos()
	{
		String texto="";
		for(SinonimoVO sinonimo : elementoSeleccionado.getSinonimos())
		{
			texto= texto.concat(sinonimo.getNombre());
			texto= texto.concat("; ");
		}
		txtSinonimos.setValue(texto);
	}

}
