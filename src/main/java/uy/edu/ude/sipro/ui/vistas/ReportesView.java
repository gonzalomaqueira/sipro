package uy.edu.ude.sipro.ui.vistas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
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
		
		boton1.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				showDetails();
			}
		});	
		
	}
	
	private void showDetails() {
        // Create a sub-window and set the content
        Window subWindow = new Window("Sub-window");
        VerticalLayout subContent = new VerticalLayout();
        subWindow.setContent(subContent);

       // File file= new File("C:\\temp\\Resumenes\\JDBC1.pdf");
        //PdfViewer c = new PdfViewer(file);
        //subContent.addComponent(c);


        // Center it in the browser window
        subWindow.setHeight("80%");
        subWindow.setWidth("60%");
        subWindow.center();
        subWindow.setModal(true);

        // Open it in the UI, but not work
        getUI().addWindow(subWindow);
	}
}
