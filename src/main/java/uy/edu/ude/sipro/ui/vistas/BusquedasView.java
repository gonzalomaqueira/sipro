package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.daishy.rangeslider.RangeSlider;
import com.github.daishy.rangeslider.client.Range;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

import uy.edu.ude.sipro.busquedas.DatosFiltro;
import uy.edu.ude.sipro.busquedas.ResultadoBusqueda;
import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.seguridad.SecurityUtils;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.service.interfaces.UsuarioService;
import uy.edu.ude.sipro.utiles.Constantes;

@SpringView
@SpringComponent
public class BusquedasView extends BusquedasViewDesign implements View
{	
	@Autowired
	private Fachada fachada;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private ArrayList<ResultadoBusqueda> resultado= new ArrayList<ResultadoBusqueda>();
	
	private DatosFiltro datosFiltro;

	private RangeSlider sliderAnio;
	private RangeSlider sliderNota;
	
	public void enter(ViewChangeEvent event) 
	{
		this.obtenerCredenciales();
		this.construirFiltro();
		this.verificarFiltros();
		
		btnBuscar.setClickShortcut(KeyCode.ENTER);
		
		btnBuscar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				try 
				{
					contenedorResultados.removeAllComponents();
					cargarDatosFiltro();
					resultado= fachada.buscarElementosProyectoES(txtBuscar.getValue(), datosFiltro);
					cargarListaComponentes(resultado);
				} catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}
		});
		
		chkFiltrar.addValueChangeListener(evt -> this.verificarFiltros());
	}
	
	public void cargarComponenteResultado(ResultadoBusqueda resultado)
	{
		Panel panel = new Panel();
		panel.setWidth("100%");
		panel.setHeight("-1px");
		
		VerticalLayout layoutVer = new VerticalLayout();
		HorizontalLayout layoutHor = new HorizontalLayout();
		layoutVer.setSizeFull();
		layoutHor.setSizeFull();
		layoutHor.setWidth("-1px");
		layoutHor.setHeight("-1px");
			
		Link linkProyecto= new Link(resultado.getTituloProyecto(), new ExternalResource("http://localhost:8080/#!proyecto-detalles/" + resultado.getIdProyecto(), "_blank"));
		Label resumenBusqueda = new Label(resultado.getResumenBusqueda(), ContentMode.HTML);
		Label codigoUde = new Label("<b>" + resultado.getCodigoUde() + "</b>", ContentMode.HTML);
		Label anio = new Label(" <i>" + resultado.getAnio() + "</i> ", ContentMode.HTML);
		Label nota= new Label("  Nota: " + resultado.getNota());
		resumenBusqueda.setWidth("100%");
		layoutVer.addComponent(linkProyecto);
		layoutHor.addComponent(codigoUde);
		layoutHor.addComponent(anio);
		layoutHor.addComponent(nota);
		layoutVer.addComponent(layoutHor);
		layoutVer.addComponent(resumenBusqueda);
		
		panel.setContent(layoutVer);
		
		this.contenedorResultados.addComponent(panel);
		
	}
	
	public void cargarListaComponentes(ArrayList<ResultadoBusqueda> resultado)
	{
		for(ResultadoBusqueda r : resultado)
		{
			if(r!=null)
				cargarComponenteResultado(r);
		}
	}
	
	public void construirFiltro()
	{	
		int anioActual = Calendar.getInstance().get(Calendar.YEAR);
		
		sliderAnio = new RangeSlider("Años", new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual), new Range(anioActual-5, anioActual));		
		sliderAnio.setSizeFull();
		sliderAnio.setWidth("280px");
		sliderAnio.setStep(1);
		layoutFiltros.addComponent(sliderAnio);
		
		sliderNota = new RangeSlider("Notas", new Range(1, 12), new Range(8, 12));
		sliderNota.setSizeFull();
		sliderNota.setWidth("280px");
		sliderNota.setStep(1);		
		layoutFiltros.addComponent(sliderNota);
	}
	
	public void verificarFiltros()
	{
		if (chkFiltrar.getValue())
    	{
    		contenedorFiltros.setVisible(true);	
    	}
    	else
    	{
    		contenedorFiltros.setVisible(false);
		}
	}
	
	public void cargarDatosFiltro()
	{
		if (chkFiltrar.getValue())
    	{
    		datosFiltro= new DatosFiltro();
    		datosFiltro.setFiltroHabilitado(true);
    		Double anioIni= sliderAnio.getValue().getLower();
    		Double anioFin= sliderAnio.getValue().getUpper();
    		Double notaIni= sliderNota.getValue().getLower();
    		Double notaFin= sliderNota.getValue().getUpper();
    		datosFiltro.setAnioIni(anioIni.intValue());
    		datosFiltro.setAnioFin(anioFin.intValue());
    		datosFiltro.setNotaIni(notaIni.intValue());
    		datosFiltro.setNotaFin(notaFin.intValue());
    		datosFiltro.setTutor(txtTutor.getValue());
    	}
    	else
    	{
    		datosFiltro= new DatosFiltro();
    		datosFiltro.setFiltroHabilitado(false);
		}
	}
	
	public void obtenerCredenciales()
	{

		Usuario user=SecurityUtils.getCurrentUser(usuarioService);

	}
	
}
