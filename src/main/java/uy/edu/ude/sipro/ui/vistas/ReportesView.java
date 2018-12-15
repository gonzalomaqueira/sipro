package uy.edu.ude.sipro.ui.vistas;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.github.daishy.rangeslider.RangeSlider;
import com.github.daishy.rangeslider.client.Range;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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
import uy.edu.ude.sipro.busquedas.BusquedaService;
import uy.edu.ude.sipro.busquedas.DatosFiltro;
import uy.edu.ude.sipro.entidades.Enumerados;
import uy.edu.ude.sipro.entidades.Enumerados.CategoriaProyectoEnum;
import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.reportes.ReportGenerator;
import uy.edu.ude.sipro.reportes.Reportes;
import uy.edu.ude.sipro.service.Fachada;

import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.ui.UIUtiles;
import uy.edu.ude.sipro.utiles.Constantes;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoReporteVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.ProyectoDetalleVO;
import uy.edu.ude.sipro.valueObjects.ProyectoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;


@SpringView
@SpringComponent
@Secured({"admin", "bibliotecario", "alumno", "tutor"})
public class ReportesView extends ReportesViewDesign implements View{
	
	@Autowired
	Fachada fachada;
	
	@Autowired
	BusquedaService busquedaService;
	
	@Autowired
	ProyectoService proyectoService;
	
	private RangeSlider sliderAnio;
	private RangeSlider sliderNota;
	
	private DatosFiltro datosFiltro;
	private DocenteVO tutorSeleccionado;
	private DocenteVO correctorSeleccionado;
	private ElementoVO elementoSeleccionado;
	private ArrayList<ElementoVO> listaElementosSeleccionados;
	private ArrayList<ElementoVO> todosElementos;
	private String elemento="";
	
	
    private final NavigationManager navigationManager;
    
    @Autowired
    public ReportesView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
	
	public void enter(ViewChangeEvent event)
	{	
		datosFiltro= new DatosFiltro();
		todosElementos= (ArrayList<ElementoVO>) fachada.obtenerElementos();
		listaElementosSeleccionados = new ArrayList<ElementoVO>();
		cargarCmbRelaciones();
		cargarCmbDocentes();
		this.construirFiltro();
		
		String tipo = event.getParameters();
		if ("personalizado".equals(tipo))
		{
			tab.setSelectedTab(reporteListados);
		}
		if ("general".equals(tipo))
		{
			tab.setSelectedTab(reportesGrafica);
		}
		
		btnLimpiar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				navigationManager.navigateTo(ReportesView.class, "personalizado");

			}
		});
		
		btnLimpiarGeneral.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				navigationManager.navigateTo(ReportesView.class, "general");

			}
		});
		
		btnAgregarRelacion.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if(elementoSeleccionado.getId()!=0)
				{
					listaElementosSeleccionados.add(elementoSeleccionado);
					btnAgregarRelacion.setEnabled(false);
					grdElementoProyecto.setItems( listaElementosSeleccionados );
					cmbElementos.clear();
					cargarCmbRelaciones();
				}
			}
		});
		
		btnEliminarRelacion.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				listaElementosSeleccionados.remove(elementoSeleccionado);
				grdElementoProyecto.setItems( listaElementosSeleccionados );
				cargarCmbRelaciones();
			}
		});
		
		cmbElementos.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	elementoSeleccionado= evt.getValue();
		    	btnAgregarRelacion.setEnabled(true);
		    }
		});
		
		cmbCorrector.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	correctorSeleccionado= evt.getValue();
		    }
		});
		
		cmbTutor.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	tutorSeleccionado= evt.getValue();
		    }
		});
		
		cmbElementoGeneral.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	elemento= evt.getValue();
		    }
		});
		
		grdElementoProyecto.addSelectionListener(evt -> 
		{
			SingleSelectionModel<ElementoVO> singleSelect = (SingleSelectionModel<ElementoVO>) grdElementoProyecto.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					elementoSeleccionado = singleSelect.getSelectedItem().get();
					btnAgregarRelacion.setEnabled(false);
					btnEliminarRelacion.setVisible(true);
				}
			}
			catch (Exception e)
			{
			}
		});

		btnGenerarReporte.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				cargarDatosFiltro();
				ArrayList<ProyectoVO> lista = cargarListaProyectos();				
				generarReporteListaProyectos(lista, datosFiltro);
			}
		});	
		
		btnGenerarReporteGeneral.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{				
				if(elemento.equals("Tecnologia"))
					generarReportesGeneral(fachada.reporteElementos(Enumerados.TipoElemento.TECNOLOGIA),
							"ESTADÍSTICA DE TECNOLOGÍAS",
							"A continuación se muestra un gráfico con la distribución de tecnologías utilizadas en los proyectos.");
				if(elemento.equals("ModeloProceso"))
					generarReportesGeneral(fachada.reporteElementos(Enumerados.TipoElemento.MODELO_PROCESO),
							"ESTADÍSTICA DE MODELOS DE PROCESO",
							"A continuación se muestra un gráfico con la distribución de modelos de proceso utilizados en los proyectos.");
				if(elemento.equals("MetodologiaTesting" ))
					generarReportesGeneral(fachada.reporteElementos(Enumerados.TipoElemento.METODOLOGIA_TESTING),
							"ESTADÍSTICA DE METODOLOGÍAS DE TESTING",
							"A continuación se muestra un gráfico con la distribución de metodologías de testin utilizadas en los proyectos.");
				if(elemento.equals(""))
					UIUtiles.mostrarNotificacion("SELECCIONE", "es necesario seleccionar un elemento", Notification.Type.HUMANIZED_MESSAGE);
			}
		});	
	}
	
	private void generarReporteListaProyectos(ArrayList<ProyectoVO> listaProyectos, DatosFiltro datosFiltro) 
	{
        StreamResource res = new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;

            @Override
            public InputStream getStream()
            {
                ByteArrayOutputStream pdfBuffer = new ByteArrayOutputStream();
                ReportGenerator reportGenerator = new ReportGenerator();
                try 
                {                    
                    if (datosFiltro.getTutorObjeto() == null)
            			datosFiltro.setTutorObjeto(new DocenteVO("",""));            		
            		if (datosFiltro.getCorrectorObjeto() == null)
            			datosFiltro.setCorrectorObjeto(new DocenteVO("",""));                    
                    
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("datosFiltro", datosFiltro);
                    if (listaProyectos.isEmpty())
                    {
                    	listaProyectos.add(new ProyectoVO(1,1,"","",1,"",Enumerados.EstadoProyectoEnum.PROCESADO, CategoriaProyectoEnum.OTRO));
                    	reportGenerator.executeReport("reportes/listaProyectosVacioTemplate.jrxml", pdfBuffer, parametros, listaProyectos);
                    }
                    else
                    {
                    	reportGenerator.executeReport("reportes/listaProyectosTemplate.jrxml", pdfBuffer, parametros, listaProyectos);
                    }
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
	
	public void generarReportesGeneral(List<ElementoReporteVO> lista, String titulo, String descripcion)
	{
    	StreamResource res = new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;
            @Override
            public InputStream getStream()
            {
                ByteArrayOutputStream pdfBuffer = new ByteArrayOutputStream();
                ReportGenerator reportGenerator = new ReportGenerator();
                try
                {
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("titulo", titulo);
                    parametros.put("descripcion", descripcion);
                    reportGenerator.executeReport("reportes/reporteElementos.jrxml", pdfBuffer, parametros, lista);
                    
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
	
	public void cargarDatosFiltro()
	{
		Double anioIni= sliderAnio.getValue().getLower();
		Double anioFin= sliderAnio.getValue().getUpper();
		Double notaIni= sliderNota.getValue().getLower();
		Double notaFin= sliderNota.getValue().getUpper();
		datosFiltro.setAnioIni(anioIni.intValue());
		datosFiltro.setAnioFin(anioFin.intValue());
		datosFiltro.setNotaIni(notaIni.intValue());
		datosFiltro.setNotaFin(notaFin.intValue());	
		datosFiltro.setTutorObjeto(tutorSeleccionado);
		datosFiltro.setCorrectorObjeto(correctorSeleccionado);
		datosFiltro.setListaElementos(listaElementosSeleccionados);		
	}	
	
	public ArrayList<ProyectoVO> cargarListaProyectos()
	{
		return (ArrayList<ProyectoVO>) fachada.obtenerListaProyectosFiltro(datosFiltro);
	}
		
	public void construirFiltro()
	{	
		int anioActual = Calendar.getInstance().get(Calendar.YEAR);
		
		sliderAnio = new RangeSlider( new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual), new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual));		
		sliderAnio.setSizeFull();
		sliderAnio.setWidth("350px");
		sliderAnio.setStep(1);
		layoutAnios.addComponent(sliderAnio);
		
		sliderNota = new RangeSlider( new Range(1, 12), new Range(1, 12));
		sliderNota.setSizeFull();
		sliderNota.setWidth("350px");
		sliderNota.setStep(1);		
		layoutNotas.addComponent(sliderNota);
	}
	
	private void cargarCmbRelaciones()
	{		
		ArrayList<ElementoVO>  aux= new ArrayList<>(todosElementos);
		for(ElementoVO elem : todosElementos)
		{
			if(listaElementosSeleccionados.contains(elem))
			{
				aux.remove(elem);
				
			}
		}
		cmbElementos.setItems(aux);
		cmbElementos.setItemCaptionGenerator(ElementoVO::getNombre);	
	}
	
	private void cargarCmbDocentes()
	{		
		List<DocenteVO> docentes=fachada.obtenerDocentes();
		cmbCorrector.setItems(docentes);
		cmbCorrector.setItemCaptionGenerator(DocenteVO::getNombreCompleto);
		cmbTutor.setItems(docentes);
		cmbTutor.setItemCaptionGenerator(DocenteVO::getNombreCompleto);	
	}
}