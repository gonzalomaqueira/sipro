package uy.edu.ude.sipro.ui.vistas;


import java.util.ArrayList;
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

import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;

@SpringView
@SpringComponent
public class ElementoDetalleView extends ElementoDetalleViewDesign implements View{
	
	@Autowired
	private Fachada fachada;
	
	//Lista con todos los elementos con su direccion correcta
	private List<ElementoVO> listaElementos;
	
	private ElementoVO elemento=null;
	
	private List<SinonimoVO> listaSinonimos= new ArrayList<SinonimoVO>();
	
	private SinonimoVO sinonimoSeleccionado;
	
	private List<SubElementoVO> listaSubElementoRelacionados= new ArrayList<SubElementoVO>();
	
	private SubElementoVO subElementoSeleccionado= new SubElementoVO();
	
    private final NavigationManager navigationManager;
    
    @Autowired
    public ElementoDetalleView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
	
	public void enter(ViewChangeEvent event) 
	{
		cargarListaElementos();
		String idElemento = event.getParameters();
		if ("".equals(idElemento))
		{
			cargarVistaGuardar();
		}
		else
		{
			cargarElemento(Integer.parseInt(idElemento));
			cargarVistaModificar();
		}		
		
		btnVolver.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				navigationManager.navigateTo(ElementoListadoView.class);
			}
		});
		
		btnGuardar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				if (txtNombreElemento.isEmpty() || chEsCategoria.isEmpty() || chTipo.isEmpty()) 
				{
					Notification.show("Hay valores vacíos", Notification.Type.WARNING_MESSAGE);
				}
				else
				{	
			    	try 
			    	{
			    		boolean esCategoria= false;
			    		if(chEsCategoria.getValue().equals("si"))
			    			esCategoria= true;
			    		
			    		//aca sacaria enumerado por string(ya que no se ve en base)
			    		TipoElemento tipo= null;
			    		if(chTipo.getValue().equals("TECNOLOGIA"))
			    		{
			    			tipo= TipoElemento.TECNOLOGIA;
			    		}
			    		if(chTipo.getValue().equals("MODELO_PROCESO"))
			    		{
			    			tipo= TipoElemento.MODELO_PROCESO;
			    		}
			    		if(chTipo.getValue().equals("METODOLOGIA_TESTING"))
			    		{
			    			tipo= TipoElemento.METODOLOGIA_TESTING;
			    		}
			    		if(chTipo.getValue().equals("OTRO"))
			    		{
			    			tipo= TipoElemento.OTRO;
			    		}
			    		
			    		fachada.altaElemento(txtNombreElemento.getValue(),
			    								esCategoria, tipo, 
			    								listaSubElementoRelacionados, listaSinonimos);
			    		Notification.show("Elemento agregado exitosamente", Notification.Type.WARNING_MESSAGE);
			    		navigationManager.navigateTo(ElementoListadoView.class);

			    	}
			    	catch (Exception e)
					{
			    		Notification.show("Hubo un error al agregar el elemento", Notification.Type.WARNING_MESSAGE);
					}

				}
			}
		});
		
		btnModificar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				if (txtNombreElemento.isEmpty() || chEsCategoria.isEmpty() || chTipo.isEmpty()) 
				{
					Notification.show("Hay valores vacíos", Notification.Type.WARNING_MESSAGE);
				}
				else
				{	
			    	try 
			    	{
			    		boolean esCategoria= false;
			    		if(chEsCategoria.getValue().equals("si"))
			    			esCategoria= true;
			    		
			    		//aca sacaria enumerado por string(ya que no se ve en base)
			    		TipoElemento tipo= null;
			    		if(chTipo.getValue().equals("TECNOLOGIA"))
			    		{
			    			tipo= TipoElemento.TECNOLOGIA;
			    		}
			    		if(chTipo.getValue().equals("MODELO_PROCESO"))
			    		{
			    			tipo= TipoElemento.MODELO_PROCESO;
			    		}
			    		if(chTipo.getValue().equals("METODOLOGIA_TESTING"))
			    		{
			    			tipo= TipoElemento.METODOLOGIA_TESTING;
			    		}
			    		if(chTipo.getValue().equals("OTRO"))
			    		{
			    			tipo= TipoElemento.OTRO;
			    		}
			    		
			    		fachada.modificarElemento(elemento.getId(),
			    								txtNombreElemento.getValue(),
			    								esCategoria, tipo, 
			    								listaSubElementoRelacionados, listaSinonimos);
			    		Notification.show("Elemento modificado exitosamente", Notification.Type.WARNING_MESSAGE);
			    		navigationManager.navigateTo(ElementoListadoView.class);

			    	}
			    	catch (Exception e)
					{
			    		Notification.show("Hubo un error al modificar el elemento", Notification.Type.WARNING_MESSAGE);
					}

				}
			}
		});
		
		cmbElementoRelacion.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	subElementoSeleccionado= new SubElementoVO();
		    	subElementoSeleccionado.setId(evt.getValue().getId());
		    	subElementoSeleccionado.setNombre(evt.getValue().getNombre());
		    	btnAgregarRelacion.setEnabled(true);
		    }
		});
		
		btnAgregarRelacion.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if(subElementoSeleccionado.getId()!=0)//ver esto, no detecta null, ver si en base nunca genera un elemento id =0!!!!!!!
				{
					listaSubElementoRelacionados.add(subElementoSeleccionado);
					grdElementoProyecto.setItems( listaSubElementoRelacionados );
					cargarCmbRelaciones();
				}
			}
		});
		
		grdElementoProyecto.addSelectionListener(evt -> 
		{
			SingleSelectionModel<SubElementoVO> singleSelect = (SingleSelectionModel<SubElementoVO>) grdElementoProyecto.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					subElementoSeleccionado = singleSelect.getSelectedItem().get();
					btnAgregarRelacion.setEnabled(false);
					btnEliminarRelacion.setVisible(true);
				}
			}
			catch (Exception e)
			{
			}
		});
		
		btnEliminarRelacion.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				listaSubElementoRelacionados.remove(subElementoSeleccionado);
				grdElementoProyecto.setItems( listaSubElementoRelacionados );
				cargarCmbRelaciones();
			}
		});
		
		btnAgregarSinonimo.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if(!txtSinonimo.isEmpty())
				{
					if (!listaContieneSinonimo(listaSinonimos, txtSinonimo.getValue()))
					{
						SinonimoVO sinonimo= new SinonimoVO();
						sinonimo.setNombre(txtSinonimo.getValue());
						listaSinonimos.add(sinonimo);
						cargarListaSinonimos();
					}
					else
					{
						Notification.show("Sinónimo ya existente", Notification.Type.WARNING_MESSAGE);
					}
				}
			}
		});
		
		btnEliminarSinonimo.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				listaSinonimos.remove(sinonimoSeleccionado);
				cargarListaSinonimos();
			}
		});
		
		grdSinonimos.addSelectionListener(evt -> 
		{
			SingleSelectionModel<SinonimoVO> singleSelect = (SingleSelectionModel<SinonimoVO>) grdSinonimos.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					sinonimoSeleccionado = singleSelect.getSelectedItem().get();
					btnEliminarSinonimo.setVisible(true);
				}
			}
			catch (Exception e)
			{
			}
		});
		
	}
	
	private void cargarVistaGuardar()
	{
		cargarCmbRelaciones();
	}
	
	private void cargarVistaModificar()
	{
		btnModificar.setVisible(true);
		btnGuardar.setVisible(false);
		txtNombreElemento.setValue(elemento.getNombre());
		cargarListaRelacionados(elemento.getId());
		cargarSinonimos();
		cargarListaSinonimos();
		cargarCmbRelaciones();
		if(elemento.isEsCategoria())
			chEsCategoria.setValue("si");
		else
			chEsCategoria.setValue("no");
		
		if(elemento.getTipoElemento().equals(TipoElemento.TECNOLOGIA))
		{
			chTipo.setValue("TECNOLOGIA");
		}
		if(elemento.getTipoElemento().equals(TipoElemento.METODOLOGIA_TESTING))
		{
			chTipo.setValue("METODOLOGIA_TESTING");
		}
		if(elemento.getTipoElemento().equals(TipoElemento.MODELO_PROCESO))
		{
			chTipo.setValue("MODELO_PROCESO");
		}
		if(elemento.getTipoElemento().equals(TipoElemento.OTRO))
		{
			chTipo.setValue("OTRO");
		}


	}
	
	private void cargarListaElementos()
	{
		listaElementos = fachada.obtenerElementos();
	}
	
	private void cargarSinonimos()
	{
		listaSinonimos= elemento.getSinonimos();
	}
	

	private void cargarListaRelacionados(int idElemento)
	{
		
		listaSubElementoRelacionados= new ArrayList<SubElementoVO>(elemento.getElementosRelacionados());
		elemento.getElementosRelacionados().removeAll(elemento.getElementosRelacionados());
		this.grdElementoProyecto.setItems( listaSubElementoRelacionados );
	}
	
	private void cargarListaSinonimos()
	{
		this.grdSinonimos.setItems( listaSinonimos );
	}
	
	private void cargarElemento(int idElemento)
	{
		for(ElementoVO elem : listaElementos)
		{
			if(elem.getId() == idElemento)
			{
				this.elemento= elem;
				break;
			}
		}
	}
	
	private void cargarCmbRelaciones()
	{		
		if(elemento != null)
		{
			//Elimina del combo relaciones a el mismo
			List<ElementoVO> relaciones= fachada.obtenerElementos();
			List<ElementoVO> relacionesAux= new ArrayList<ElementoVO>(relaciones);
			for(ElementoVO elem : relacionesAux)
			{
				if(elem.getId()==elemento.getId())
				{
					relaciones.remove(elem);
				}
				
				for(SubElementoVO subElem : listaSubElementoRelacionados)
				{
					if(subElem.getId()==elem.getId())
					{
						relaciones.remove(elem);
					    break;
					}
				}
			}
			cmbElementoRelacion.setItems(relaciones);
			cmbElementoRelacion.setItemCaptionGenerator(ElementoVO::getNombre);
			cmbElementoRelacion.setValue(null);
			btnAgregarRelacion.setEnabled(false);
		}
		else
		{
			cmbElementoRelacion.setItems(fachada.obtenerElementos());
			cmbElementoRelacion.setItemCaptionGenerator(ElementoVO::getNombre);
		}
	}
	
	private boolean listaContieneSinonimo(List<SinonimoVO> listaSinonimos, String sinonimoBuscado)
	{
		for (SinonimoVO sinonimo : listaSinonimos)
		{
			if (sinonimo.getNombre().equals(sinonimoBuscado))
			{					
				return true;
			}
		}
		return false;
	}
	
}
