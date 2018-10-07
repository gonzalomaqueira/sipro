package uy.edu.ude.sipro.ui.vistas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
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
		String data1 = "Mag. Miguel Rojas";	       
		String regex = "\\brojas\\b";
		 
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(data1);
		if (matcher.find())
			System.out.println("Encontro");
		
		
	}

}
