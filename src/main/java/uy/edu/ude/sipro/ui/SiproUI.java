package uy.edu.ude.sipro.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;


import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;


@SpringUI
@Theme("siprotema")
@DesignRoot
public class SiproUI extends UI{

	
	private final SpringViewProvider viewProvider;
	
	private final NavigationManager navigationManager;

	private final MainView mainView;
		
	@Autowired
	private Fachada fachada;
	
	@Autowired
	public SiproUI(SpringViewProvider viewProvider, NavigationManager navigationManager, MainView mainView) {
		this.viewProvider = viewProvider;
		this.navigationManager = navigationManager;
		this.mainView = mainView;
	}

	@Override
	protected void init(VaadinRequest request) {
		
		setContent(mainView);

		navigationManager.navigateToDefaultView();			
		
	}

	
}
