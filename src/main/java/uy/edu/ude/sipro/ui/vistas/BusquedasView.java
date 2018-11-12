package uy.edu.ude.sipro.ui.vistas;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import uy.edu.ude.sipro.ui.componentes.ResultadoBusqueda;

@SpringView
@SpringComponent
public class BusquedasView extends BusquedasViewDesign implements View{
	
	public void enter(ViewChangeEvent event) 
	{			
		ResultadoBusqueda resultado1 = new ResultadoBusqueda(1, "Nombre primer proyecto", "Este es el asaaaaaaaa aaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaaaa aaaaaaa aaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaapropio <b>resumen</b> que resulta de la busqueda", 23);
		ResultadoBusqueda resultado2 = new ResultadoBusqueda(2, "Nombre segundo proyecto", "... a miguel le gusta miguelear...", 215);
		
		List<ResultadoBusqueda> lista = new ArrayList<ResultadoBusqueda>();
		lista.add(resultado1);
		lista.add(resultado2);
		lista.add(resultado1);
		lista.add(resultado2);
		lista.add(resultado1);
		lista.add(resultado2);
		lista.add(resultado1);
		lista.add(resultado2);
		lista.add(resultado1);
		lista.add(resultado2);
		lista.add(resultado1);
		lista.add(resultado2);
		
		for(ResultadoBusqueda r : lista)
		{
			cargarComponenteResultado(r);
		}
	}
	
	public void cargarComponenteResultado(ResultadoBusqueda resultado)
	{
		Panel panel = new Panel();
		panel.setWidth("100%");
		panel.setHeight("-1px");
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		
		Link linkProyecto= new Link(resultado.getTituloProyecto(), new ExternalResource("http://vaadin.com/"));
		Label resumenBusqueda = new Label(resultado.getResumenBusqueda(), ContentMode.HTML);
		resumenBusqueda.setWidth("100%");
		layout.addComponent(linkProyecto);
		layout.addComponent(resumenBusqueda);
		
		panel.setContent(layout);
		
		this.contenedorResultados.addComponent(panel);
		
	}
}
