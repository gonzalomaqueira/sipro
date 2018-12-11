package uy.edu.ude.sipro.ui.vistas;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import net.sf.jasperreports.engine.JRException;
import uy.edu.ude.sipro.busquedas.BusquedaService;
import uy.edu.ude.sipro.busquedas.DatosFiltro;
import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.reportes.ReportGenerator;
import uy.edu.ude.sipro.reportes.Reportes;
import uy.edu.ude.sipro.service.Fachada;

import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.utiles.Constantes;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.ProyectoVO;


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
	
	public void enter(ViewChangeEvent event)
	{	
		this.construirFiltro();
		
		ArrayList<ProyectoVO> listaResultado= new ArrayList<ProyectoVO>();
		ProyectoVO proyecto1= new ProyectoVO(1, 2014, "#123", "Licenciatura en informatica", 12, "SIPRO, el mejor proyecto del mundo mundial", EstadoProyectoEnum.PROCESADO);
		ProyectoVO proyecto2= new ProyectoVO(2, 2017, "#234", "Ingeniería en informática", 9, "Ingenierizando la facultad de la UDE", EstadoProyectoEnum.PROCESADO);
		ProyectoVO proyecto3= new ProyectoVO(3, 2012, "#456", "Manualidad avanzada", 11, "Miguel pintando", EstadoProyectoEnum.PROCESADO);
		listaResultado.add(proyecto1);
		listaResultado.add(proyecto2);
		listaResultado.add(proyecto3);
		
		btnGenerarReporte.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				
				DatosFiltro datosFiltro = new DatosFiltro();
				datosFiltro.setAnioIni(2011);
				datosFiltro.setAnioFin(2018);
				datosFiltro.setNotaIni(7);
				datosFiltro.setNotaFin(12);
				datosFiltro.setTutor("Sirely");
				List<DocenteVO> listaCorrectores = new ArrayList<>();
				listaCorrectores.add(new DocenteVO("Miguel","Rojas"));
				listaCorrectores.add(new DocenteVO("Ariel","Ron"));
				datosFiltro.setListaCorrectores(listaCorrectores);
				List<ElementoVO> listaElementos = new ArrayList<>();
				listaElementos.add(new ElementoVO("Java"));
				listaElementos.add(new ElementoVO("Microsoft SQL Server"));
				datosFiltro.setListaElementos(listaElementos);
				
	
				generarReporteListaProyectos(listaResultado, datosFiltro);
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
	
	public void construirFiltro()
	{	
		int anioActual = Calendar.getInstance().get(Calendar.YEAR);
		
		sliderAnio = new RangeSlider("Años", new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual), new Range(anioActual-5, anioActual));		
		sliderAnio.setSizeFull();
		sliderAnio.setWidth("350px");
		sliderAnio.setStep(1);
		layoutFiltros.addComponent(sliderAnio);
		
		sliderNota = new RangeSlider("Notas", new Range(1, 12), new Range(8, 12));
		sliderNota.setSizeFull();
		sliderNota.setWidth("350");
		sliderNota.setStep(1);		
		layoutFiltros.addComponent(sliderNota);
	}
}