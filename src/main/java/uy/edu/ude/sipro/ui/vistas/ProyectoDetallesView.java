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

import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.utiles.FuncionesTexto;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.ProyectoDetalleVO;

@SpringView
@SpringComponent
public class ProyectoDetallesView extends ProyectoDetallesViewDesign implements View
{
	@Autowired
	private Fachada fachada;

	private ProyectoDetalleVO proyecto;
    private final NavigationManager navigationManager;
    
    @Autowired
    public ProyectoDetallesView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
	
	public void enter(ViewChangeEvent event)
	{
		cargarInterfazInicial();
		String idProyecto = event.getParameters();
		if ("".equals(idProyecto))
		{
			cargarVistaNuevoProyecto();
		}
		else
		{
			cargarVistaModificarProyecto(Integer.parseInt(idProyecto));
		}
		
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
															proyecto.getCorrector(),
															proyecto.getNota(),
															proyecto.getResumen(),
															proyecto.getAlumnos(),
															proyecto.getTutor());
						
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
	}
	
	private void cargarVistaNuevoProyecto()
	{
		btnGuardar.setVisible(true);
		btnEditar.setVisible(false);
	}
	
	private void cargarVistaModificarProyecto(int idProyecto)
	{
		proyecto = fachada.obtenerProyectoPorId(idProyecto);
		if (proyecto != null)
		{
			txtNombreProyecto.setValue(proyecto.getNombre());
			txtCarrera.setValue(proyecto.getCarrera() != null ? proyecto.getCarrera() : "");
			txtCorrector.setValue(proyecto.getCorrector() != null ? proyecto.getCorrector() : "");
			txtNota.setValue(Integer.toString(proyecto.getNota()));
			txtAnio.setValue(Integer.toString(proyecto.getAnio()));
			txtTutor.setValue(FuncionesTexto.convertirArrayAStringSaltoLinea(proyecto.getTutor()));
			txtAlumnos.setValue(FuncionesTexto.convertirArrayAStringSaltoLinea(proyecto.getAlumnos()));
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
	}
	
	private void cargarInterfazModificar()
	{
		btnEditar.setVisible(false);
		btnGuardar.setVisible(true);
		permitirEdicion(true);
	}
	
	private void cargarDatosProyecto()
	{
		proyecto.setNombre(txtNombreProyecto.getValue());
		proyecto.setCarrera(txtCarrera.getValue());
		proyecto.setCorrector(txtCorrector.getValue());
		proyecto.setNota( Integer.parseInt(txtNota.getValue()) );
		proyecto.setAnio( Integer.parseInt(txtAnio.getValue()) );
		proyecto.setTutor(FuncionesTexto.convertirStringAArrayList(txtTutor.getValue()));
		proyecto.setAlumnos(FuncionesTexto.convertirStringAArrayList(txtAlumnos.getValue()));
		proyecto.setResumen(txtResumen.getValue());
	}
	
	private void permitirEdicion(boolean opcion)
	{
		
		if(opcion)
		{
			this.txtNombreProyecto.setReadOnly(false);
			this.txtCarrera.setReadOnly(false);
			this.txtCorrector.setReadOnly(false);
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
			this.txtCorrector.setReadOnly(true);
			this.txtNota.setReadOnly(true);
			this.txtAnio.setReadOnly(true);
			this.txtTutor.setReadOnly(true);
			this.txtAlumnos.setReadOnly(true);
			this.txtResumen.setReadOnly(true);
		}	
	}
}
