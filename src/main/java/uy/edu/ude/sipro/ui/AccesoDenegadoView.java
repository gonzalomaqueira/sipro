package uy.edu.ude.sipro.ui;


import org.springframework.context.annotation.Scope;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;

@SpringComponent
@Scope("prototype")
public class AccesoDenegadoView extends AccesoDenegadoViewDesign implements View {

	@Override
	public void enter(ViewChangeEvent event) {
		// Nothing to do, just show the view
	}

}