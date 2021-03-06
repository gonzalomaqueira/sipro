package uy.edu.ude.sipro.ui.vistas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import net.sf.jasperreports.engine.JRException;
import uy.edu.ude.sipro.entidades.Enumerados.CategoriaProyectoEnum;
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.reportes.ReportGenerator;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.utiles.FuncionesTexto;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.ProyectoDetalleVO;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

@SpringView
@SpringComponent
@Secured({"Administrador", "Bibliotecario", "Invitado", "Alumno", "Tutor", "Docente"})
public class ProyectoDetallesView extends ProyectoDetallesViewDesign implements View
{
	@Autowired
	private Fachada fachada;

	private UsuarioVO usuario;
	private ProyectoDetalleVO proyecto;
    private DocenteVO correctorSeleccionado;
    private ElementoVO elementoSeleccionado;
    private Binder<ProyectoDetalleVO> binder;
    private List<DocenteVO> listaCorrectores;
    private List<DocenteVO> listaDocentes;
    private List<ElementoVO> listaElementosProyecto;
    private List<ElementoVO> listaElementosTotal;
    private final NavigationManager navigationManager;
    
    @Autowired
    public ProyectoDetallesView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    	this.listaCorrectores = new ArrayList<DocenteVO>();	
    	this.listaDocentes= new ArrayList<DocenteVO>();
    	this.listaElementosProyecto= new ArrayList<ElementoVO>();
    	this.listaElementosTotal= new ArrayList<ElementoVO>();
    }
	
	public void enter(ViewChangeEvent event)
	{
		btnEditar.setVisible(false);
		
		usuario = fachada.obtenerUsuarioLogeado();
		if(!usuario.getPerfil().getDescripcion().equals("Administrador") && !usuario.getPerfil().getDescripcion().equals("Tutor"))
		{
			layoutBotones.setVisible(false);
			layoutBotones.setEnabled(false);
		}
		
		cargarInterfazInicial();
		this.listaDocentes= fachada.obtenerDocentes();
		this.listaElementosTotal= fachada.obtenerElementos().stream()
				.filter(x -> !x.isEsCategoria() && x.getTipoElemento() != TipoElemento.OTRO).collect(Collectors.toList());

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
		cargarComboElementos();
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
															proyecto.getBibliografia(),
															proyecto.getCategoria(),
															proyecto.getElementosRelacionados());
							
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
		
		btnGenerarReporte.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				generarReporte(proyecto);
			}
		}); 
		
		
		cmbCorrectores.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	correctorSeleccionado= evt.getValue();
		    	btnAgregarCorrector.setEnabled(true);
		    	btnEliminarCorrector.setVisible(false);
		    	grdCorrectores.deselectAll();
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
		
		cmbElementos.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	elementoSeleccionado= evt.getValue();
		    	btnAgregarElemento.setEnabled(true);
		    	btnEliminarElemento.setVisible(false);
		    	grdTecnologias.deselectAll();
				grdModeloProceso.deselectAll();
				grdMetodologiaTesting.deselectAll();
		    }
		});
		
		grdMetodologiaTesting.addSelectionListener(evt -> 
		{
			SingleSelectionModel<ElementoVO> singleSelect = (SingleSelectionModel<ElementoVO>) grdMetodologiaTesting.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					grdTecnologias.deselectAll();
					grdModeloProceso.deselectAll();
					elementoSeleccionado = singleSelect.getSelectedItem().get();
					btnAgregarElemento.setEnabled(false);
					btnEliminarElemento.setVisible(true);
					btnEliminarElemento.setEnabled(true);
				}
			}
			catch (Exception e)
			{
			}
		});
		
		grdModeloProceso.addSelectionListener(evt -> 
		{
			SingleSelectionModel<ElementoVO> singleSelect = (SingleSelectionModel<ElementoVO>) grdModeloProceso.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					grdTecnologias.deselectAll();
					grdMetodologiaTesting.deselectAll();
					elementoSeleccionado = singleSelect.getSelectedItem().get();
					btnAgregarElemento.setEnabled(false);
					btnEliminarElemento.setVisible(true);
					btnEliminarElemento.setEnabled(true);
				}
			}
			catch (Exception e)
			{
			}
		});
		
		grdTecnologias.addSelectionListener(evt -> 
		{
			SingleSelectionModel<ElementoVO> singleSelect = (SingleSelectionModel<ElementoVO>) grdTecnologias.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					grdMetodologiaTesting.deselectAll();
					grdModeloProceso.deselectAll();
					elementoSeleccionado = singleSelect.getSelectedItem().get();
					btnAgregarElemento.setEnabled(false);
					btnEliminarElemento.setVisible(true);
					btnEliminarElemento.setEnabled(true);
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
				if(correctorSeleccionado.getId()!=0)
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
		
		btnAgregarElemento.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if(elementoSeleccionado.getId()!=0)
				{
					listaElementosProyecto.add(elementoSeleccionado);					
					cargarGrillasElementos();
					cargarComboElementos();
					
				}
			}
		});
		
		btnEliminarElemento.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				listaElementosProyecto.remove(elementoSeleccionado);				
				cargarGrillasElementos();				
				btnEliminarElemento.setEnabled(false);
				cargarComboElementos();
			}
		});
	}
	
	private void cargarGrillasElementos()
	{
		grdTecnologias.setItems(listaElementosProyecto.stream()
				.filter(x -> x.getTipoElemento() == TipoElemento.TECNOLOGIA)
				.sorted()
				.collect(Collectors.toList()));
		grdModeloProceso.setItems(listaElementosProyecto.stream()
				.filter(x -> x.getTipoElemento() == TipoElemento.MODELO_PROCESO)
				.sorted()
				.collect(Collectors.toList()));
		grdMetodologiaTesting.setItems(listaElementosProyecto.stream()
				.filter(x -> x.getTipoElemento() == TipoElemento.METODOLOGIA_TESTING)
				.sorted()
				.collect(Collectors.toList()));
	}	
	
	private void cargarVistaNuevoProyecto()
	{
		btnGuardar.setVisible(true);
		btnEditar.setVisible(false);
		cmbCategoria.setValue(CategoriaProyectoEnum.OTRO);
	}
	
	private void cargarVistaModificarProyecto(int idProyecto)
	{
		proyecto = fachada.obtenerProyectoPorId(idProyecto);
		listaCorrectores= proyecto.getCorrectores();
		listaElementosProyecto= proyecto.getElementosRelacionados();
		actualizarListaCorrectores();
		actualizarListaElementos();
		if (proyecto != null)
		{
			txtCodigo.setValue(proyecto.getCodigoUde());
			txtCarrera.setValue(proyecto.getCarrera() != null ? proyecto.getCarrera() : "");
			grdCorrectores.setItems(listaCorrectores);
			txtNota.setValue(Integer.toString(proyecto.getNota()));
			txtAnio.setValue(Integer.toString(proyecto.getAnio()));
			txtTitulo.setValue(proyecto.getTitulo());
			cmbCategoria.setValue(proyecto.getCategoria());
			txtTutor.setValue(proyecto.getTutorString() != null ? FuncionesTexto.convertirArrayAStringSaltoLinea(new ArrayList<String>(proyecto.getTutorString())) : "");
			txtAlumnos.setValue(proyecto.getAlumnos() != null ? FuncionesTexto.convertirArrayAStringSaltoLinea(new ArrayList<String>(proyecto.getAlumnos())) : "");
			txtBibliografia.setValue(proyecto.getBibliografia() != null ? FuncionesTexto.convertirArrayAStringSaltoLinea(new ArrayList<String>(proyecto.getBibliografia())) : "");
			txtResumen.setValue(proyecto.getResumen() != null ? proyecto.getResumen() : "");
			cargarGrillasElementos();
		}
		this.expandirTextAreas();
	}

	private void expandirTextAreas()
	{
		if (txtBibliografia != null && !txtBibliografia.getValue().equals(""))
			txtBibliografia.setRows(proyecto.getBibliografia().size() + 1);
		
		if (txtAlumnos != null && !txtAlumnos.getValue().equals(""))
			txtAlumnos.setRows(proyecto.getAlumnos().size() + 1);
		
		if (txtTutor != null && !txtTutor.getValue().equals(""))
			txtTutor.setRows(proyecto.getTutorString().size() + 1);
	}

	private void cargarInterfazInicial()
	{
		List<CategoriaProyectoEnum> listaCategorias = new ArrayList<>();
		listaCategorias.add(CategoriaProyectoEnum.DESARROLLO);
		listaCategorias.add(CategoriaProyectoEnum.INFRAESTRUCTURA);
		listaCategorias.add(CategoriaProyectoEnum.OTRO);
		cmbCategoria.setItems(listaCategorias);

		permitirEdicion(false);
		btnEditar.setVisible(true);
		btnGuardar.setVisible(false);
		btnCancelar.setVisible(false);
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
		proyecto.setCategoria(cmbCategoria.getValue());
		proyecto.setElementosRelacionados(listaElementosProyecto);
	}
	
	private void permitirEdicion(boolean opcion)
	{
		if(opcion)
		{
			this.layoutCorrectores.setVisible(true);
			this.layoutElementos.setVisible(true);
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
			this.cmbCategoria.setReadOnly(false);
		}
		else
		{
			this.layoutCorrectores.setVisible(false);
			this.layoutElementos.setVisible(false);
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
			this.cmbCategoria.setReadOnly(true);
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
	
	private void cargarComboElementos()
	{
		actualizarListaElementos();
		Set<ElementoVO> elementosCombo = new HashSet<ElementoVO>(listaElementosTotal);
		Set<ElementoVO> elementoAux = new HashSet<ElementoVO>(elementosCombo);
		for(ElementoVO cor : elementoAux)
		{				
			for(ElementoVO corAux : listaElementosProyecto)
			{
				if(cor.getId()==corAux.getId())
				{
					elementosCombo.remove(cor);
				    break;
				}
			}
		}
		cmbElementos.setItems(elementosCombo);
		cmbElementos.setItemCaptionGenerator(ElementoVO::getNombre);
		cmbElementos.setValue(null);
		btnAgregarElemento.setEnabled(false);

	}
	
	private void actualizarListaElementos()
	{
		Set<ElementoVO> elementosAux = new HashSet<ElementoVO>(listaElementosProyecto);
		listaElementosProyecto= new ArrayList<ElementoVO>();
		if(elementosAux!=null)
		{
			for (ElementoVO elem : elementosAux)
			{
				for (ElementoVO elemTot : listaElementosTotal) 
				{
					if (elem.getId() == elemTot.getId())
					{
						listaElementosProyecto.add(elemTot);
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
	
	private void generarReporte(ProyectoDetalleVO proyecto) 
	{
        StreamResource res = new StreamResource(new StreamResource.StreamSource() 
        {
            private static final long serialVersionUID = 1L;

            @Override
            public InputStream getStream() {
                ByteArrayOutputStream pdfBuffer = new ByteArrayOutputStream();
                ReportGenerator reportGenerator = new ReportGenerator();

                try 
                {
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("proyecto",  proyecto);
                    reportGenerator.executeReportNoDS("reportes/proyectoTemplate.jrxml", pdfBuffer, parametros);
                    
                } catch (Exception e) 
                {
					e.printStackTrace();
				}
                return new ByteArrayInputStream(pdfBuffer.toByteArray());
            }
        }, "Documento" + Math.random() + ".pdf");
        setResource("descarga", res);
        ResourceReference rr = ResourceReference.create(res, this, "descarga");
        Page.getCurrent().open(rr.getURL(), "_blank");
    }


}
