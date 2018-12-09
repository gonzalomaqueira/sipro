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

import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.reportes.ReportGenerator;
import uy.edu.ude.sipro.reportes.Reportes;
import uy.edu.ude.sipro.service.Fachada;

import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.utiles.Constantes;
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
	
	public void enter(ViewChangeEvent event)
	{	
		this.construirFiltro();
		
		ArrayList<ProyectoVO> lista= new ArrayList<ProyectoVO>();
		ProyectoVO proyecto1= new ProyectoVO(1, 1, "Lic Informatica", "El proyecto de miguel1", 12, "El proyecto de miguel1", EstadoProyectoEnum.PROCESADO);
		ProyectoVO proyecto2= new ProyectoVO(2, 2, "Lic Informatica", "El proyecto de miguel2", 12, "El proyecto de miguel2", EstadoProyectoEnum.PROCESADO);
		ProyectoVO proyecto3= new ProyectoVO(3, 3, "Lic Informatica", "El proyecto de miguel3", 12, "El proyecto de miguel3", EstadoProyectoEnum.PROCESADO);
		lista.add(proyecto1);
		lista.add(proyecto2);
		lista.add(proyecto3);
		
		btnGenerarReporte.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
//				Reportes reporte= new  Reportes();
//				try {
//					reporte.generarReporte(lista);
//					Page.getCurrent().open("C:\\reportes\\reporte1.pdf", "_blank");
//				} catch (FileNotFoundException | JRException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				generarReporteEjemplo(lista);
				
			}
		});	
	}
	
	private void generarReporteEjemplo(ArrayList<ProyectoVO> listaProyectos) {
        StreamResource res = new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;

            @Override
            public InputStream getStream() {
                ByteArrayOutputStream pdfBuffer = new ByteArrayOutputStream();
                ReportGenerator reportGenerator = new ReportGenerator();

                try {
//                    reportGenerator.executeReport("/reportes/reporteTemplate.jrxml", pdfBuffer, null, listaProyectos);
                	
                    Map<String, Object> parametros = new HashMap<>();
                	reportGenerator.executeReport("reportes/reporteTemplate.jrxml", pdfBuffer, parametros, listaProyectos);
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
		sliderAnio.setWidth("280px");
		sliderAnio.setStep(1);
		cssFiltros.addComponent(sliderAnio);
		
		sliderNota = new RangeSlider("Notas", new Range(1, 12), new Range(8, 12));
		sliderNota.setSizeFull();
		sliderNota.setWidth("280px");
		sliderNota.setStep(1);		
		cssFiltros.addComponent(sliderNota);
	}
}