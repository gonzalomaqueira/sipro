package uy.edu.ude.sipro.ui.vistas;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
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
public class UsuarioDetalleViewDesign extends VerticalLayout {
	protected Button btnVolver;
	protected Button btnGuardar;
	protected Button btnCancelar;
	protected TextField txtUsuario;
	protected TextField txtContrasenia;
	protected TextField txtRepetirContasenia;
	protected Label lblContraseñasIguales;
	protected Label lblContraseñasIncorrectas;
	protected TextField txtNombre;
	protected TextField txtApellido;
	protected TextField txtEmail;
	protected ComboBox<uy.edu.ude.sipro.valueObjects.PerfilVO> cmbPerfil;

	public UsuarioDetalleViewDesign() {
		Design.read(this);
	}
}
