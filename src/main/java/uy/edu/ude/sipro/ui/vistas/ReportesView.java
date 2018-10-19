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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;



import com.vaadin.ui.Button.ClickEvent;

import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.service.interfaces.ElementoService;


@SpringView
@SpringComponent
public class ReportesView extends ReportesViewDesign implements View{
	
	@Autowired
	Fachada fachada;
	
	@Autowired
	ElementoService elementoService;
	
	public void enter(ViewChangeEvent event)
	{				        
		BrowserWindowOpener opener = new BrowserWindowOpener(new FileResource(new File("src/main/resources/documentos/JDBC1.pdf")));
		opener.setWindowName("_blank");
		opener.extend(boton1);
		
		link= new Link("hola", new FileResource(new File("../documentos/JDBC1.pdf")));
		this.addComponent(link);
		
	}
}
