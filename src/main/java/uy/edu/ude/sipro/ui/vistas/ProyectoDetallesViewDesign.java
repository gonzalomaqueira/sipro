package uy.edu.ude.sipro.ui.vistas;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
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
public class ProyectoDetallesViewDesign extends VerticalLayout {
	protected HorizontalLayout layoutBotones;
	protected Button btnEditar;
	protected Button btnGuardar;
	protected Button btnCancelar;
	protected Button btnVerTextoDocumento;
	protected Button btnGenerarReporte;
	protected CssLayout tabGeneral;
	protected HorizontalLayout contenedorAnioNota;
	protected TextField txtCodigo;
	protected TextField txtAnio;
	protected TextField txtNota;
	protected TextField txtTitulo;
	protected TextField txtCarrera;
	protected ComboBox<uy.edu.ude.sipro.entidades.Enumerados.CategoriaProyectoEnum> cmbCategoria;
	protected HorizontalLayout contenedorTutorAlumnos;
	protected TextArea txtTutor;
	protected TextArea txtAlumnos;
	protected CssLayout tabResumen;
	protected TextArea txtResumen;
	protected CssLayout tabCorrectores;
	protected HorizontalLayout layoutCorrectores;
	protected ComboBox<uy.edu.ude.sipro.valueObjects.DocenteVO> cmbCorrectores;
	protected Button btnAgregarCorrector;
	protected Button btnEliminarCorrector;
	protected Grid<uy.edu.ude.sipro.valueObjects.DocenteVO> grdCorrectores;
	protected CssLayout tabElementos;
	protected HorizontalLayout layoutElementos;
	protected ComboBox<uy.edu.ude.sipro.valueObjects.ElementoVO> cmbElementos;
	protected Button btnAgregarElemento;
	protected Button btnEliminarElemento;
	protected Grid<uy.edu.ude.sipro.valueObjects.ElementoVO> grdTecnologias;
	protected Grid<uy.edu.ude.sipro.valueObjects.ElementoVO> grdModeloProceso;
	protected Grid<uy.edu.ude.sipro.valueObjects.ElementoVO> grdMetodologiaTesting;
	protected CssLayout tabBiblio;
	protected TextArea txtBibliografia;

	public ProyectoDetallesViewDesign() {
		Design.read(this);
	}
}
