package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
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
@Secured({"Admin", "Bibliotecario", "Invitado", "Alumno", "Tutor"})
public class BusquedasView extends BusquedasViewDesign implements View
{	
	@Autowired
	private Fachada fachada;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private ArrayList<ResultadoBusqueda> resultado= new ArrayList<ResultadoBusqueda>();
	private Window popUp;
	private DatosFiltro datosFiltro;
	private TextField txtTutor= new TextField();
	private TextField txtCorrector= new TextField();
	private TextField txtBiblio= new TextField();
	Button btnAplicarFiltros = new Button("Aplicar");
	Button btnLimpiarFiltros = new Button("Limpiar");
	private RangeSlider sliderAnio;
	private RangeSlider sliderNota;
	
	public void enter(ViewChangeEvent event) 
	{
		layoutSinResultados.setVisible(false);
		datosFiltro= new DatosFiltro();
		datosFiltro.setFiltroHabilitado(false);
		
		btnBuscar.setClickShortcut(KeyCode.ENTER);
		
		btnBuscar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				try 
				{
					contenedorResultados.removeAllComponents();
					if(chkFiltros.getValue())
					{
						resultado= fachada.buscarElementosProyectoES(txtBuscar.getValue(), datosFiltro);
					}
					else
					{
						DatosFiltro datosFiltroAux= new DatosFiltro();
						datosFiltroAux.setFiltroHabilitado(false);
						resultado= fachada.buscarElementosProyectoES(txtBuscar.getValue(), datosFiltroAux);
					}
					cargarListaComponentes(resultado);
				} catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}
		});
		
		btnFiltrar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				desplegarPopUP();
			}
		});
		
		btnAplicarFiltros.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
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
	    		datosFiltro.setTutorString(txtTutor.getValue());
	    		datosFiltro.setBibliografia(txtBiblio.getValue());
	    		datosFiltro.setCorrectorString(txtCorrector.getValue());
	    		popUp.close();
	    		chkFiltros.setValue(true);
			}
		});
		
		btnLimpiarFiltros.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				datosFiltro= new DatosFiltro();
				datosFiltro.setFiltroHabilitado(false);
				int anioActual = Calendar.getInstance().get(Calendar.YEAR);
				sliderAnio = new RangeSlider("Años", new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual), new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual));		
				sliderNota = new RangeSlider("Notas", new Range(1, 12), new Range(1, 12));
				txtCorrector.clear();
				txtTutor.clear();
				txtBiblio.clear();
				popUp.close();
				desplegarPopUP();			
			}
		});
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
			
		Link linkProyecto= new Link(resultado.getTituloProyecto(), new ExternalResource("http://localhost:8080/#!proyecto-detalles/" + resultado.getIdProyecto()));
		linkProyecto.setTargetName("_blank");
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
		if (resultado != null && !resultado.isEmpty())
		{
			layoutSinResultados.setVisible(false);
			for(ResultadoBusqueda r : resultado)
			{
				if(r!=null)
					cargarComponenteResultado(r);
			}
		}
		else
		{
			layoutSinResultados.setVisible(true);
		}
	}
	
	public void desplegarPopUP()
	{
		HorizontalLayout hTutor= new HorizontalLayout();
		Label lblTutor= new Label("Tutor:");
		lblTutor.setWidth("80px");
		txtTutor.setWidth("260px");
		hTutor.addComponent(lblTutor);
		hTutor.addComponent(txtTutor);
		
		HorizontalLayout hCorrector= new HorizontalLayout();
		Label lblCorrector= new Label("Corrector:");
		lblCorrector.setWidth("80px");
		txtCorrector.setWidth("260px");
		hCorrector.addComponent(lblCorrector);
		hCorrector.addComponent(txtCorrector);
		
		HorizontalLayout hBiblio= new HorizontalLayout();
		Label lblBiblio= new Label("Bibliografía:");
		lblBiblio.setWidth("80px");
		txtBiblio.setWidth("260px");
		hBiblio.addComponent(lblBiblio);
		hBiblio.addComponent(txtBiblio);
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(hTutor);
		layout.setComponentAlignment(hTutor, Alignment.TOP_CENTER);
		layout.addComponent(hCorrector);
		layout.setComponentAlignment(hCorrector, Alignment.TOP_CENTER);
		layout.addComponent(hBiblio);
		layout.setComponentAlignment(hBiblio, Alignment.TOP_CENTER);
		
		int anioActual = Calendar.getInstance().get(Calendar.YEAR);
		if(sliderAnio==null)
			sliderAnio = new RangeSlider(new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual), new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual));
		else
			sliderAnio = new RangeSlider(new Range(Constantes.ANIO_INICIO_BUSQUEDA, anioActual), new Range(sliderAnio.getValue().getLower(), sliderAnio.getValue().getUpper()));
		
		layout.addComponent(new CssLayout());
		
		HorizontalLayout hAnios= new HorizontalLayout();
		Label lblAnios= new Label("Años:");
		lblAnios.setWidth("80px");
		sliderAnio.setSizeFull();
		sliderAnio.setWidth("260px");
		sliderAnio.setStep(1);
		hAnios.addComponent(lblAnios);
		hAnios.addComponent(sliderAnio);
		layout.addComponent(hAnios);
		layout.setComponentAlignment(hAnios, Alignment.TOP_CENTER);
		
		if(sliderNota==null)
			sliderNota = new RangeSlider(new Range(1, 12), new Range(1, 12));
		else
			sliderNota = new RangeSlider(new Range(1, 12), new Range(sliderNota.getValue().getLower(), sliderNota.getValue().getUpper()));
		
		layout.addComponent(new CssLayout());
		
		HorizontalLayout hNotas= new HorizontalLayout();
		Label lblNotas= new Label("Notas:");
		lblNotas.setWidth("80px");
		sliderNota.setSizeFull();
		sliderNota.setWidth("260px");
		sliderNota.setStep(1);		
		hNotas.addComponent(lblNotas);
		hNotas.addComponent(sliderNota);
		layout.addComponent(hNotas);
		layout.setComponentAlignment(hNotas, Alignment.TOP_CENTER);
		
		HorizontalLayout hBotones= new HorizontalLayout();
		hBotones.addComponent(btnAplicarFiltros);
		hBotones.setComponentAlignment(btnAplicarFiltros, Alignment.BOTTOM_CENTER);
		
		hBotones.addComponent(btnLimpiarFiltros);
		hBotones.setComponentAlignment(btnLimpiarFiltros, Alignment.BOTTOM_CENTER);
		
		layout.addComponent(hBotones);
		layout.setComponentAlignment(hBotones, Alignment.BOTTOM_CENTER);
		
		layout.setMargin(true);
		popUp = new Window("Filtros", layout);
		popUp.setModal(true);
		popUp.setResizable(false);
		popUp.center();
		popUp.setHeight("400");
		popUp.setWidth("400");
		UI.getCurrent().addWindow(popUp);
	}
}
