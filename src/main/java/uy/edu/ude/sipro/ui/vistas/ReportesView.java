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
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.reportes.ReportGenerator;
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
@Secured({"Administrador", "Bibliotecario", "Alumno", "Tutor", "Docente"})
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
		btnAgregarRelacion.setEnabled(false);
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
				btnEliminarRelacion.setVisible(false);
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
				
				if (lista == null)
				{
					lista = new ArrayList<>();
				}
				generarReporteListaProyectos(lista, datosFiltro);
			}
		});	
		
		btnGenerarReporteGeneral.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				try
				{
					boolean topDiez = checkTopDies.getValue();
					List<ElementoReporteVO> listaElementosReporte = new ArrayList<>();
					if(elemento.equals("Tecnologia"))
					{
						listaElementosReporte = fachada.reporteElementos(Enumerados.TipoElemento.TECNOLOGIA);
						if (topDiez)
						{
							listaElementosReporte = listaElementosReporte.stream().sorted().limit(10).collect(Collectors.toList());
						}						
						String mensaje = topDiez
								? "A continuación se muestra un listado con la cantidad de proyectos que utilizan cada tecnología. Se visualiza el TOP 10."
								: "A continuación se muestra un listado con la cantidad de proyectos que utilizan cada tecnología.";					 
						
						generarReportesGeneral(listaElementosReporte, "ESTADÍSTICA DE TECNOLOGÍAS", mensaje, "Tecnología", topDiez);
					}
					if(elemento.equals("ModeloProceso"))
					{
						listaElementosReporte = fachada.reporteElementos(Enumerados.TipoElemento.MODELO_PROCESO);
						String mensaje = topDiez
								? "A continuación se muestra un listado con la cantidad de proyectos que utilizan cada modelo de proceso. Se visualiza el TOP 10."
								: "A continuación se muestra un listado con la cantidad de proyectos que utilizan cada modelo de proceso.";	
						
						generarReportesGeneral(listaElementosReporte, "ESTADÍSTICA DE MODELOS DE PROCESO", mensaje, "Modelo de proceso", topDiez);
					}
					if(elemento.equals("MetodologiaTesting"))
					{
						listaElementosReporte = fachada.reporteElementos(Enumerados.TipoElemento.METODOLOGIA_TESTING);
						String mensaje = topDiez
								? "A continuación se muestra un listado con la cantidad de proyectos que utilizan cada metodología de testing. Se visualiza el TOP 10."
								: "A continuación se muestra un listado con la cantidad de proyectos que utilizan cada metodología de testing.";	
						
						generarReportesGeneral(listaElementosReporte, "ESTADÍSTICA DE METODOLOGÍAS DE TESTING", mensaje, "Metodología de testing", topDiez);
					}
					if(elemento.equals(""))
						UIUtiles.mostrarNotificacion("SELECCIONE", "Es necesario seleccionar un elemento", Notification.Type.HUMANIZED_MESSAGE);
				
				}
				catch (Exception e)
				{
					UIUtiles.mostrarNotificacion("ERROR", "Ocurrió un error al generar el reporte", Notification.Type.ERROR_MESSAGE);
				}
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

                    if (!listaProyectos.isEmpty())
                    {
                    	reportGenerator.executeReport("reportes/listaProyectosTemplate.jrxml", pdfBuffer, parametros, listaProyectos);
                    }
                    else
                    {
                    	listaProyectos.add(new ProyectoVO(1,1,"","",1,"",Enumerados.EstadoProyectoEnum.PROCESADO, CategoriaProyectoEnum.OTRO));
                    	reportGenerator.executeReport("reportes/listaProyectosVacioTemplate.jrxml", pdfBuffer, parametros, listaProyectos);                    	
                    }
                } 
                catch (Exception e) 
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
	
	public void generarReportesGeneral(List<ElementoReporteVO> lista, String titulo, String descripcion, String tipoElemento, boolean topDies)
	{
		String reporte = topDies ? "reportes/reporteElementosTop10.jrxml" : "reportes/reporteElementosListado.jrxml";
		
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
                    parametros.put("tipoElemento", tipoElemento);
                    reportGenerator.executeReport(reporte, pdfBuffer, parametros, lista);                    
                } 
                catch (Exception e)
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