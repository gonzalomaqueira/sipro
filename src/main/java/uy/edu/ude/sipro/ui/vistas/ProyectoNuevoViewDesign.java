package uy.edu.ude.sipro.ui.vistas;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
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
public class ProyectoNuevoViewDesign extends VerticalLayout {
	protected Button btnVolver;
	protected Button btnGuardar;
	protected Button btnCancelar;
	protected TextField txtCodigoUde;
	protected TextField txtNota;
	protected TextField txtCarrera;
	protected ComboBox<uy.edu.ude.sipro.valueObjects.DocenteVO> cmbCorrectores;
	protected Button btnAgregarCorrector;
	protected Button btnEliminarCorrector;
	protected Grid<uy.edu.ude.sipro.valueObjects.DocenteVO> grdCorrectores;
	protected Upload updSubirProyecto;

	public ProyectoNuevoViewDesign() {
		Design.read(this);
	}
}
