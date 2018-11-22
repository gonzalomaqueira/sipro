package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.InternalResourceView;

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
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

import uy.edu.ude.sipro.busquedas.ResultadoBusqueda;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.service.Fachada;

@SpringView
@SpringComponent
public class BusquedasView extends BusquedasViewDesign implements View{
	
	@Autowired
	private Fachada fachada;
	ArrayList<ResultadoBusqueda> resultado= new ArrayList<ResultadoBusqueda>();
	
	public void enter(ViewChangeEvent event) 
	{							
		
		btnBuscar.setClickShortcut(KeyCode.ENTER);
		
		btnBuscar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				try {
					contenedorResultados.removeAllComponents();
					resultado= fachada.buscarElementosProyectoES(txtBuscar.getValue());
					cargarListaComponentes(resultado);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
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
			
		Link linkProyecto= new Link(resultado.getTituloProyecto(), new ExternalResource("http://localhost:8080/#!proyecto-detalles/" + resultado.getIdProyecto(), "_blank"));
		Label resumenBusqueda = new Label(resultado.getResumenBusqueda(), ContentMode.HTML);
		Label codigoUde = new Label("- <b> " + resultado.getCodigoUde() + "</b> ", ContentMode.HTML);
		Label anio = new Label("<i>" + resultado.getAnio() + "</i> ", ContentMode.HTML);
		resumenBusqueda.setWidth("100%");
		layoutHor.addComponent(linkProyecto);
		layoutHor.addComponent(codigoUde);
		layoutVer.addComponent(layoutHor);
		layoutVer.addComponent(anio);
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
}
