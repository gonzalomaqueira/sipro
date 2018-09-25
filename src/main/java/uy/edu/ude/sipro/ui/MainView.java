package uy.edu.ude.sipro.ui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewLeaveAction;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.ui.vistas.ElementoDetalleView;
import uy.edu.ude.sipro.ui.vistas.ElementoListadoView;
import uy.edu.ude.sipro.ui.vistas.ProyectoListadoView;
import uy.edu.ude.sipro.ui.vistas.ReportesView;
import uy.edu.ude.sipro.ui.vistas.UsuarioDetalleView;
import uy.edu.ude.sipro.ui.vistas.UsuarioListadoView;



@SpringViewDisplay
@UIScope
public class MainView extends MainViewDesign implements ViewDisplay {
	
	private final Map<Class<? extends View>, Button> navigationButtons = new HashMap<>();
	private final NavigationManager navigationManager;
	//private final SecuredViewAccessControl viewAccessControl;

	@Autowired
	public MainView(NavigationManager navigationManager /*, SecuredViewAccessControl viewAccessControl*/) {
		this.navigationManager = navigationManager;
		//this.viewAccessControl = viewAccessControl;
	}

	@PostConstruct
	public void init() {
		attachNavigation(proyectos, ProyectoListadoView.class);
		attachNavigation(usuarios, UsuarioListadoView.class);
		attachNavigation(tecnologias, ElementoListadoView.class);
		attachNavigation(reportes, ReportesView.class);
		attachNavigation(configuracion, UsuarioDetalleView.class);


		salir.addClickListener(e -> logout());
	}

	/**
	 * Makes clicking the given button navigate to the given view if the user
	 * has access to the view.
	 * <p>
	 * If the user does not have access to the view, hides the button.
	 *
	 * @param navigationButton
	 *            the button to use for navigatio
	 * @param targetView
	 *            the view to navigate to when the user clicks the button
	 */
	private void attachNavigation(Button navigationButton, Class<? extends View> targetView) {
		//boolean hasAccessToView = viewAccessControl.isAccessGranted(targetView);
		//((navigationButton.setVisible(hasAccessToView);

		//if (hasAccessToView) {
			navigationButtons.put(targetView, navigationButton);
			navigationButton.addClickListener(e -> navigationManager.navigateTo(targetView));
		//}
	}

	@Override
	public void showView(View view) {
		content.removeAllComponents();
		content.addComponent(view.getViewComponent());

		navigationButtons.forEach((viewClass, button) -> button.setStyleName("selected", viewClass == view.getClass()));

		Button menuItem = navigationButtons.get(view.getClass());
		String viewName = "";
		if (menuItem != null) {
			viewName = menuItem.getCaption();
		}
	}

	/**
	 * Logs the user out after ensuring the currently open view has no unsaved
	 * changes.
	 */
	public void logout() {
		ViewLeaveAction doLogout = () -> {
			UI ui = getUI();
			ui.getSession().getSession().invalidate();
			ui.getPage().reload();
		};

		navigationManager.runAfterLeaveConfirmation(doLogout);
	}

	public NavigationManager getNavigationManager() {
		return navigationManager;
	}
	
	

}

