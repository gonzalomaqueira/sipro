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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import net.sf.jasperreports.engine.JRException;
import uy.edu.ude.sipro.busquedas.BusquedaService;
import uy.edu.ude.sipro.busquedas.DatosFiltro;
import uy.edu.ude.sipro.entidades.Enumerados;
import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.reportes.ReportGenerator;
import uy.edu.ude.sipro.reportes.Reportes;
import uy.edu.ude.sipro.service.Fachada;

import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.utiles.Constantes;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.ProyectoDetalleVO;
import uy.edu.ude.sipro.valueObjects.ProyectoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;


@SpringView
@SpringComponent
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
	
	public void enter(ViewChangeEvent event)
	{	
		datosFiltro= new DatosFiltro();
		todosElementos= (ArrayList<ElementoVO>) fachada.obtenerElementos();
		listaElementosSeleccionados = new ArrayList<ElementoVO>();
		cargarCmbRelaciones();
		cargarCmbDocentes();
		this.construirFiltro();
		
//		ArrayList<ProyectoVO> listaResultado= new ArrayList<ProyectoVO>();
//		ProyectoVO proyecto1= new ProyectoVO(1, 2014, "#123", "Licenciatura en informatica", 12, "SIPRO, el mejor proyecto del mundo mundial", EstadoProyectoEnum.PROCESADO);
//		ProyectoVO proyecto2= new ProyectoVO(2, 2017, "#234", "Ingeniería en informática", 9, "Ingenierizando la facultad de la UDE", EstadoProyectoEnum.PROCESADO);
//		ProyectoVO proyecto3= new ProyectoVO(3, 2012, "#456", "Manualidad avanzada", 11, "Miguel pintando", EstadoProyectoEnum.PROCESADO);
//		listaResultado.add(proyecto1);
//		listaResultado.add(proyecto2);
//		listaResultado.add(proyecto3);
		
		btnAgregarRelacion.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if(elementoSeleccionado.getId()!=0)//ver esto, no detecta null, ver si en base nunca genera un elemento id =0!!!!!!!
				{
					listaElementosSeleccionados.add(elementoSeleccionado);
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
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("datosFiltro", datosFiltro);
                	reportGenerator.executeReport("reportes/listaProyectosTemplate.jrxml", pdfBuffer, parametros, listaProyectos);
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
//		datosFiltro.setTutorObjeto(new DocenteVO("Miguel", "Rojas"));
//		datosFiltro.setCorrector(new DocenteVO("Ariel", "Ron"));
//		datosFiltro.setTutorObjeto(tutorSeleccionado);
//		datosFiltro.setCorrector(correctorSeleccionado);
//		datosFiltro.setListaElementos(listaElementosSeleccionados);
		
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