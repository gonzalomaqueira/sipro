package uy.edu.ude.sipro.ui.vistas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;



import com.vaadin.ui.Button.ClickEvent;

import uy.edu.ude.sipro.busquedas.BusquedaService;
import uy.edu.ude.sipro.busquedas.ResultadoBusqueda;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.service.implementacion.ProyectoServiceImp;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.service.interfaces.ProyectoService;


@SpringView
@SpringComponent
public class ReportesView extends ReportesViewDesign implements View{
	
	@Autowired
	Fachada fachada;
	
	@Autowired
	BusquedaService busquedaService;
	
	@Autowired
	ProyectoService proyectoService;
	
	public void enter(ViewChangeEvent event)
	{	

		// Create the first tab
		VerticalLayout tab1 = new VerticalLayout();
		
		tab1.addComponent(new Label("Tab1"));
		tab.addTab(tab1, "titulo tab1");

		VerticalLayout tab2 = new VerticalLayout();
		
		tab2.addComponent(new Label("Tab2"));
		tab.addTab(tab2, "titulo tab2");
		
		VerticalLayout tab3 = new VerticalLayout();
		
		tab3.addComponent(new Label("Tab3"));
		tab.addTab(tab3, "titulo tab3");

		
//		try {
//			
//			fachada.actualizarSinonimosElemntosES();
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}