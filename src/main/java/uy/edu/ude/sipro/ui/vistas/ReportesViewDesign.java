package uy.edu.ude.sipro.ui.vistas;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class ReportesViewDesign extends VerticalLayout {
	protected ComboBox<java.lang.String> cmbTutor;
	protected ComboBox<java.lang.String> cmbElemento;
	protected VerticalLayout cssFiltros;
	protected Button btnGenerarReporte;

	public ReportesViewDesign() {
		Design.read(this);
	}
}
