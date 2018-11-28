package uy.edu.ude.sipro.ui.vistas;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.utiles.FuncionesTexto;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.ProyectoDetalleVO;
import uy.edu.ude.sipro.valueObjects.ProyectoVO;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

@SpringView
@SpringComponent
public class ProyectoDetallesView extends ProyectoDetallesViewDesign implements View
{
	@Autowired
	private Fachada fachada;

	private ProyectoDetalleVO proyecto;
    private DocenteVO correctorSeleccionado;
    private Binder<ProyectoDetalleVO> binder;
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
			
			BrowserWindowOpener opener = new BrowserWindowOpener(new FileResource(new File(proyecto.getRutaArchivo())));
			opener.setWindowName("_blank");
			opener.extend(btnVerTextoDocumento);
		}
		cargarCmbCorrectores();
		this.SetearBinder();
		btnGuardar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{										
				try 
				{
					BinderValidationStatus<ProyectoDetalleVO> statusValidacion = binder.validate();
		    		if (statusValidacion.isOk())
		    		{
                        cargarDatosProyecto();
						fachada.modificarProyectoCompleto( 	proyecto.getId(),
															proyecto.getCodigoUde(),
															proyecto.getTitulo(),
															proyecto.getAnio(),
															proyecto.getCarrera(),
															proyecto.getNota(),
															proyecto.getResumen(),
															proyecto.getAlumnos(),
															proyecto.getTutorString(),															
															proyecto.getCorrectores(),
															proyecto.getBibliografia());
							
						
						 UIUtiles.mostrarNotificacion("PROYECTO", "Modificación exitosa", Notification.Type.HUMANIZED_MESSAGE);	
					     cargarInterfazInicial();
					}
		    		else
		    		{
		    			UIUtiles.mostrarErrorValidaciones(statusValidacion.getValidationErrors());
		    		}
		    	}
		    	catch (Exception e)
				{
		    		UIUtiles.mostrarNotificacion("ERROR", "Ocurrió un error al modificar proyecto", Notification.Type.WARNING_MESSAGE);	
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
				btnCancelar.setVisible(false);
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
			txtCodigo.setValue(proyecto.getCodigoUde());
			txtCarrera.setValue(proyecto.getCarrera() != null ? proyecto.getCarrera() : "");
			grdCorrectores.setItems(proyecto.getCorrectores());
			txtNota.setValue(Integer.toString(proyecto.getNota()));
			txtAnio.setValue(Integer.toString(proyecto.getAnio()));
			txtTitulo.setValue(proyecto.getTitulo());
			txtTutor.setValue(proyecto.getTutorString() != null ? FuncionesTexto.convertirArrayAStringSaltoLinea(new ArrayList<String>(proyecto.getTutorString())) : "");
			txtAlumnos.setValue(proyecto.getAlumnos() != null ? FuncionesTexto.convertirArrayAStringSaltoLinea(new ArrayList<String>(proyecto.getAlumnos())) : "");
			txtBibliografia.setValue(proyecto.getBibliografia() != null ? FuncionesTexto.convertirArrayAStringSaltoLinea(new ArrayList<String>(proyecto.getBibliografia())) : "");
			txtResumen.setValue(proyecto.getResumen() != null ? proyecto.getResumen() : "");
			grdTecnologias.setItems(this.obtenerElementosPorTipo(proyecto.getElementosRelacionados(), TipoElemento.TECNOLOGIA));
			grdMetodologiaTesting.setItems(this.obtenerElementosPorTipo(proyecto.getElementosRelacionados(), TipoElemento.METODOLOGIA_TESTING));
			grdModeloProceso.setItems(this.obtenerElementosPorTipo(proyecto.getElementosRelacionados(), TipoElemento.MODELO_PROCESO));
		}
		this.expandirTextAreas();
	}

	private void expandirTextAreas()
	{
		txtBibliografia.setRows(proyecto.getBibliografia().size() + 1);
		txtAlumnos.setRows(proyecto.getAlumnos().size() + 1);
		txtTutor.setRows(proyecto.getTutorString().size() + 1);
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
		btnCancelar.setVisible(true);
		cmbCorrectores.setEnabled(true);
		permitirEdicion(true);
	}
	
	private void cargarDatosProyecto()
	{
		proyecto.setCodigoUde(txtCodigo.getValue());
		proyecto.setCarrera(txtCarrera.getValue());
		proyecto.setNota( Integer.parseInt(txtNota.getValue()));
		proyecto.setAnio( Integer.parseInt(txtAnio.getValue()));
		proyecto.setTitulo(txtTitulo.getValue());
		proyecto.setCorrectores(listaCorrectores);
		proyecto.setTutorString(FuncionesTexto.convertirStringAArrayList(txtTutor.getValue()));
		proyecto.setAlumnos(FuncionesTexto.convertirStringAArrayList(txtAlumnos.getValue()));
		proyecto.setResumen(txtResumen.getValue());
		proyecto.setBibliografia(FuncionesTexto.convertirStringAArrayList(txtBibliografia.getValue()));
	}
	
	private void permitirEdicion(boolean opcion)
	{
		if(opcion)
		{
			this.txtCarrera.setReadOnly(false);
			this.grdCorrectores.setEnabled(true);
			this.cmbCorrectores.setEnabled(true);
			this.txtNota.setReadOnly(false);
			this.txtAnio.setReadOnly(false);
			this.txtTitulo.setReadOnly(false);
			this.txtTutor.setReadOnly(false);
			this.txtAlumnos.setReadOnly(false);
			this.txtResumen.setReadOnly(false);
			this.txtBibliografia.setReadOnly(false);
		}
		else
		{
			this.txtCarrera.setReadOnly(true);
			this.grdCorrectores.setEnabled(false);
			this.cmbCorrectores.setEnabled(false);
			this.txtNota.setReadOnly(true);
			this.txtAnio.setReadOnly(true);
			this.txtTitulo.setReadOnly(true);
			this.txtTutor.setReadOnly(true);
			this.txtAlumnos.setReadOnly(true);
			this.txtResumen.setReadOnly(true);
			this.txtBibliografia.setReadOnly(true);
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
	
	private void SetearBinder()
	{
		binder = new Binder<ProyectoDetalleVO>(ProyectoDetalleVO.class);

		binder.forField(txtTitulo)
		.withValidator(titulo -> titulo.length() >= 1, "Título no puede estar vacío")
		.bind(ProyectoDetalleVO::getTitulo, ProyectoDetalleVO::setTitulo);
		
        binder.forField(txtAnio)
        	.withValidator(anio ->FuncionesTexto.esNumerico(anio), "El año tiene que ser numérico")
        	.withValidator(anio -> anio.length() == 4, "El año debe tener 4 dígitos")
        	.bind(ProyectoDetalleVO::getAnioString, ProyectoDetalleVO::setAnioString);
        
        binder.forField(txtNota)
    		.withValidator(nota ->FuncionesTexto.esNumerico(nota), "La nota tiene que ser numérico")
    		.withValidator(nota ->FuncionesTexto.esNotaValida(nota), "La nota debe estar entre 0 y 12")
        	.bind(ProyectoDetalleVO::getNotaString, ProyectoDetalleVO::setNotaString);


        
        binder.readBean(proyecto);
	}


}
