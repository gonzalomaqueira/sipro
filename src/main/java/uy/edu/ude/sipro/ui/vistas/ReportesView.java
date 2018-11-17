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

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.service.implementacion.ProyectoServiceImp;
import uy.edu.ude.sipro.service.interfaces.BusquedaService;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.ui.componentes.ResultadoBusqueda;


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
		ArrayList<Elemento> resultado= busquedaService.obtenerElementoString("Vaadin tiene jaVa y un poco de caja negra ");	
		System.out.println("Resulado: \n \n");
		for(Elemento elem : resultado)
		{
			System.out.println(elem.getNombre() + "\n");
		}
		try {
			String resultado2= busquedaService.buscarElementosProyectoES(resultado);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}