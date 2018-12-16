package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.dialogs.ConfirmDialog;

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
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;

@SpringView
@SpringComponent
@Secured("Admin")
public class ElementoListadoView extends ElementoListadoViewDesign implements View{
	
	
	@Autowired
    private Fachada fachada;
	
	private ElementoVO elementoSeleccionado;
	
	private final NavigationManager navigationManager;
	
	private List<ElementoVO> listaElementos;
	
    @Autowired
    public ElementoListadoView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
	
	public void enter(ViewChangeEvent event) 
	{		
		btnVerDetalles.setEnabled(false);
		cargarListaElementos();
		btnEliminar.setEnabled(false);
		
		txtBuscar.addValueChangeListener(evt -> 
		{
		    filtrarLista(evt.getValue());
		});
		
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
					btnVerDetalles.setEnabled(true);
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
					ConfirmDialog.show(UI.getCurrent(), "Confirmación:", "¿Seguro que desea eliminar?",
					        "Eliminar", "Cancelar", new ConfirmDialog.Listener() {

					            public void onClose(ConfirmDialog dialog)
					            {
					                if (dialog.isConfirmed())
					                {
					                	try
										{
											fachada.eliminarElemento(elementoSeleccionado.getId());
											UIUtiles.mostrarNotificacion("ELEMENTO", "Baja exitosa", Notification.Type.HUMANIZED_MESSAGE);
											navigationManager.navigateTo(ElementoListadoView.class);
										}
										catch (Exception e)
										{
											UIUtiles.mostrarNotificacion("ELEMENTOS", "Ocurrió un error al eliminar el elemento", Notification.Type.ERROR_MESSAGE);
										}		    					
					                } else {
					                }
					            }
					        });				
				}
			}
		});		
	}
	
	private void cargarListaElementos()
	{
		listaElementos=fachada.obtenerElementos();
		this.grdElementos.setItems(listaElementos);
	}
	
	private void cargarListaRelaciones()
	{
		String texto="";
		for(SubElementoVO elemento : elementoSeleccionado.getElementosRelacionados())
		{
			texto= texto.concat(elemento.getNombre());
			texto= texto.concat("; ");
		}
		if(texto.length()>=2)
			texto= texto.substring(0,texto.length()-2);
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
		if(texto.length()>=2)
			texto= texto.substring(0,texto.length()-2);
		txtSinonimos.setValue(texto);
	}
	
	private void filtrarLista(String texto) 
	{
		List<ElementoVO> listaAux = new ArrayList<>();
		for (ElementoVO elemento : this.listaElementos)
		{
			if (elemento.getNombre().toLowerCase().contains(texto.toLowerCase().trim()))
			{
				listaAux.add(elemento);
			}
		}
		grdElementos.setItems(listaAux);
	}

}
