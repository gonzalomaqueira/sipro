package uy.edu.ude.sipro.ui.vistas;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;

@SpringView
@SpringComponent
public class ContenidoView extends ContenidoViewDesign implements View {
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		btnDamien.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				btnDamien.setCaption("Prueba");
				
			}
		});	
	}
	
	
	
}
