package uy.edu.ude.sipro.navegacion;

import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;

import uy.edu.ude.sipro.ui.vistas.BusquedasView;

/*************************************************************************

Clase que define el controlador de navegación entre vistas

**************************************************************************/
@Component
@UIScope
public class NavigationManager extends SpringNavigator
{
	public String getViewId(Class<? extends View> viewClass)
	{
		SpringView springView = viewClass.getAnnotation(SpringView.class);
		if (springView == null) {
			throw new IllegalArgumentException("The target class must be a @SpringView");
		}

		return Conventions.deriveMappingForView(viewClass, springView);
	}

	public void navigateTo(Class<? extends View> targetView)
	{
		String viewId = getViewId(targetView);
		navigateTo(viewId);
	}

	public void navigateTo(Class<? extends View> targetView, Object parameter)
	{
		String viewId = getViewId(targetView);
		navigateTo(viewId + "/" + parameter.toString());		
	}

	public void navigateToDefaultView()
	{
		if (!getState().isEmpty()) {
			return;
		}

		navigateTo(BusquedasView.class);
	}

	public void updateViewParameter(String parameter)
	{
		String viewName = getViewId(getCurrentView().getClass());
		String parameters;
		if (parameter == null) {
			parameters = "";
		} else {
			parameters = parameter;
		}

		updateNavigationState(new ViewChangeEvent(this, getCurrentView(), getCurrentView(), viewName, parameters));
	}
}
