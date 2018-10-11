package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import uy.edu.ude.sipro.utiles.FuncionesTexto;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.ProyectoDetalleVO;

@SpringView
@SpringComponent
public class ProyectoDetallesView extends ProyectoDetallesViewDesign implements View
{
	@Autowired
	private Fachada fachada;

	private ProyectoDetalleVO proyecto;
    private DocenteVO correctorSeleccionado;
    private List<DocenteVO> listaCorrectores;
    private List<DocenteVO> listaDocentes;
    private final NavigationManager navigationManager;
    
    @Autowired
    public ProyectoDetallesView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    	this.listaCorrectores = new ArrayList<DocenteVO>();	
    	this.listaDocentes= new ArrayList<DocenteVO>();
    }
	
	public void enter(ViewChangeEvent event)
	{
		cargarInterfazInicial();
		this.listaDocentes= fachada.obtenerDocentes();

		String idProyecto = event.getParameters();
		if ("".equals(idProyecto))
		{
			cargarVistaNuevoProyecto();
		}
		else
		{
			cargarVistaModificarProyecto(Integer.parseInt(idProyecto));
		}
		cargarCmbCorrectores();
		
		btnGuardar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{										
				try 
				{
					if( !txtNombreProyecto.isEmpty() && !txtAnio.isEmpty() && !txtCarrera.isEmpty() && !txtNota.isEmpty() && !txtResumen.isEmpty() 
						&& !txtAlumnos.isEmpty() && !txtTutor.isEmpty())
					{
						cargarDatosProyecto();
						fachada.modificarProyectoCompleto( 	proyecto.getId(), 
															proyecto.getNombre(),
															proyecto.getAnio(),
															proyecto.getCarrera(),
															proyecto.getNota(),
															proyecto.getResumen(),
															proyecto.getAlumnos(),
															proyecto.getTutorString(),															
															proyecto.getCorrectores());
							
						
					     Notification.show("Proyecto modificado exitosamente", Notification.Type.HUMANIZED_MESSAGE);
					     cargarInterfazInicial();
					}
					else
						Notification.show("Hay campos vacíos", Notification.Type.HUMANIZED_MESSAGE); 
		    	}
		    	catch (Exception e)
				{
		    		Notification.show("Hubo un error al modificar proyecto",Notification.Type.WARNING_MESSAGE);
		    		e.printStackTrace();
					cargarVistaModificarProyecto(proyecto.getId());		
				}
			}
		});
		
		btnEditar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{						
				cargarInterfazModificar();
			}
		});
		
		btnCancelar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if ("".equals(idProyecto))
				{
					navigationManager.navigateTo(ProyectoDetallesView.class);
				}
				else
				{
					navigationManager.navigateTo(ProyectoDetallesView.class, idProyecto);
				}					
			}
		}); 
		
		btnVolver.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				navigationManager.navigateTo(ProyectoListadoView.class);
			}
		});
		
		cmbCorrectores.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	correctorSeleccionado= evt.getValue();
		    	btnAgregarCorrector.setEnabled(true);
		    }
		});
		
		grdCorrectores.addSelectionListener(evt -> 
		{
			SingleSelectionModel<DocenteVO> singleSelect = (SingleSelectionModel<DocenteVO>) grdCorrectores.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					correctorSeleccionado = singleSelect.getSelectedItem().get();
					btnAgregarCorrector.setEnabled(false);
					btnEliminarCorrector.setVisible(true);
					btnEliminarCorrector.setEnabled(true);
				}
			}
			catch (Exception e)
			{
			}
		});
		
		btnAgregarCorrector.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if(correctorSeleccionado.getId()!=0)//ver esto, no detecta null, ver si en base nunca genera un elemento id =0!!!!!!!
				{
					listaCorrectores.add(correctorSeleccionado);
					grdCorrectores.setItems(listaCorrectores);
					cargarCmbCorrectores();
					
				}
			}
		});
		
		btnEliminarCorrector.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				listaCorrectores.remove(correctorSeleccionado);
				grdCorrectores.setItems( listaCorrectores );
				btnEliminarCorrector.setEnabled(false);
				cargarCmbCorrectores();
				
			}
		});
	}
	
	private void cargarVistaNuevoProyecto()
	{
		btnGuardar.setVisible(true);
		btnEditar.setVisible(false);
	}
	
	private void cargarVistaModificarProyecto(int idProyecto)
	{
		proyecto = fachada.obtenerProyectoPorId(idProyecto);
		listaCorrectores= proyecto.getCorrectores();
		actualizarListaCorrectores();
		if (proyecto != null)
		{
			txtNombreProyecto.setValue(proyecto.getNombre());
			txtCarrera.setValue(proyecto.getCarrera() != null ? proyecto.getCarrera() : "");
			grdCorrectores.setItems(proyecto.getCorrectores());
			txtNota.setValue(Integer.toString(proyecto.getNota()));
			txtAnio.setValue(Integer.toString(proyecto.getAnio()));
			txtTutor.setValue(proyecto.getTutorString() != null ? FuncionesTexto.convertirArrayAStringSaltoLinea(new ArrayList<String>(proyecto.getTutorString())) : "");
			txtAlumnos.setValue(proyecto.getAlumnos() != null ? FuncionesTexto.convertirArrayAStringSaltoLinea(new ArrayList<String>(proyecto.getAlumnos())) : "");
			txtResumen.setValue(proyecto.getResumen() != null ? proyecto.getResumen() : "");
			grdTecnologias.setItems(this.obtenerElementosPorTipo(proyecto.getElementosRelacionados(), TipoElemento.TECNOLOGIA));
			grdMetodologiaTesting.setItems(this.obtenerElementosPorTipo(proyecto.getElementosRelacionados(), TipoElemento.METODOLOGIA_TESTING));
			grdModeloProceso.setItems(this.obtenerElementosPorTipo(proyecto.getElementosRelacionados(), TipoElemento.MODELO_PROCESO));
		}
	}

	private List<ElementoVO> obtenerElementosPorTipo(List<ElementoVO> elementosRelacionados, TipoElemento tipo)
	{
		List<ElementoVO> vRetorno = new ArrayList<ElementoVO>();
		for (ElementoVO elem : elementosRelacionados)
		{
			if (elem.getTipoElemento() == tipo)
				vRetorno.add(elem);
		}
		return vRetorno;
	}

	private void cargarInterfazInicial()
	{
		permitirEdicion(false);
		btnEditar.setVisible(true);
		btnGuardar.setVisible(false);
		cmbCorrectores.setEnabled(false);
	}
	
	private void cargarInterfazModificar()
	{
		btnEditar.setVisible(false);
		btnGuardar.setVisible(true);
		cmbCorrectores.setEnabled(true);
		permitirEdicion(true);
	}
	
	private void cargarDatosProyecto()
	{
		proyecto.setNombre(txtNombreProyecto.getValue());
		proyecto.setCarrera(txtCarrera.getValue());
		proyecto.setNota( Integer.parseInt(txtNota.getValue()));
		proyecto.setAnio( Integer.parseInt(txtAnio.getValue()));
		proyecto.setCorrectores(listaCorrectores);
		proyecto.setTutorString(FuncionesTexto.convertirStringAArrayList(txtTutor.getValue()));
		proyecto.setAlumnos(FuncionesTexto.convertirStringAArrayList(txtAlumnos.getValue()));
		proyecto.setResumen(txtResumen.getValue());
	}
	
	private void permitirEdicion(boolean opcion)
	{
		if(opcion)
		{
			this.txtNombreProyecto.setReadOnly(false);
			this.txtCarrera.setReadOnly(false);
			this.grdCorrectores.setEnabled(true);
			this.cmbCorrectores.setEnabled(true);
			this.txtNota.setReadOnly(false);
			this.txtAnio.setReadOnly(false);
			this.txtTutor.setReadOnly(false);
			this.txtAlumnos.setReadOnly(false);
			this.txtResumen.setReadOnly(false);
		}
		else
		{
			this.txtNombreProyecto.setReadOnly(true);
			this.txtCarrera.setReadOnly(true);
			this.grdCorrectores.setEnabled(false);
			this.cmbCorrectores.setEnabled(false);
			this.txtNota.setReadOnly(true);
			this.txtAnio.setReadOnly(true);
			this.txtTutor.setReadOnly(true);
			this.txtAlumnos.setReadOnly(true);
			this.txtResumen.setReadOnly(true);
		}	
	}
	
	private void cargarCmbCorrectores()
	{
		actualizarListaCorrectores();
		Set<DocenteVO> correctoresCombo = new HashSet<DocenteVO>(listaDocentes);
		Set<DocenteVO> correctoresAux = new HashSet<DocenteVO>(correctoresCombo);
		for(DocenteVO cor : correctoresAux)
		{				
			for(DocenteVO corAux : listaCorrectores)
			{
				if(cor.getId()==corAux.getId())
				{
					correctoresCombo.remove(cor);
				    break;
				}
			}
		}
		cmbCorrectores.setItems(correctoresCombo);
		cmbCorrectores.setItemCaptionGenerator(DocenteVO::getNombreCompleto);
		cmbCorrectores.setValue(null);
		btnAgregarCorrector.setEnabled(false);

	}
	
	private void actualizarListaCorrectores()
	{
		Set<DocenteVO> correctoresAux = new HashSet<DocenteVO>(listaCorrectores);
		listaCorrectores= new ArrayList<DocenteVO>();
		if(correctoresAux!=null)
		{
			for (DocenteVO cor : correctoresAux)
			{
				for (DocenteVO doc : listaDocentes) 
				{
					if (cor.getId() == doc.getId())
					{
						listaCorrectores.add(doc);
					}
				}				
			}
		}
	}

}
