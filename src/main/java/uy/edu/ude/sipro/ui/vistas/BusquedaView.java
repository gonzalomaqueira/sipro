package uy.edu.ude.sipro.ui.vistas;


import org.springframework.context.annotation.Scope;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;



@SpringView
@SpringComponent
@Scope("prototype")
public class BusquedaView extends BusquedaViewDesign implements View {
	

	
	@Override
	public void enter(ViewChangeEvent event)
	{
		
	}

}
