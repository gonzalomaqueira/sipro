package uy.edu.ude.sipro.ui.vistas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.Notification;

import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.utiles.Constantes;
import uy.edu.ude.sipro.utiles.ReceptorArchivos;
import uy.edu.ude.sipro.valueObjects.ProyectoVO;


@SpringView
@SpringComponent
public class ProyectoNuevoView extends ProyectoNuevoViewDesign implements View
{
	@Autowired
	private Fachada fachada;

    private final NavigationManager navigationManager;
    
    private String nombreArchivo;
    private String prefijoArchivo;

    
    @Autowired
    public ProyectoNuevoView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    }
	
	public void enter(ViewChangeEvent event)
	{
		cargarInterfazInicial();		
		setearInformacionUpload();
		
		btnGuardar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				updSubirProyecto.submitUpload();
			}
		});
		
		
		updSubirProyecto.addFinishedListener(evt ->
		{
			nombreArchivo= evt.getFilename();           
			try 
			{
				fachada.altaProyecto(txtNombreProyecto.getValue(),
									 txtCarrera.getValue(),
									 txtCorrector.getValue(),
									 Integer.parseInt(txtNota.getValue()),
									 Constantes.RUTA_ARCHIVOS + prefijoArchivo + nombreArchivo);
			   
				Notification.show("Archivo subido exitosamente", Notification.Type.HUMANIZED_MESSAGE);           
			}
			catch (Exception e)
			{
				Notification.show("Hubo un error al subir el proyecto", Notification.Type.WARNING_MESSAGE);
				e.printStackTrace();
			}
			navigationManager.navigateTo(ProyectoListadoView.class);
        });
		
		
		btnCancelar.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				navigationManager.navigateTo(ProyectoNuevoView.class);		
			}
		}); 
		
		
		btnVolver.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				navigationManager.navigateTo(ProyectoListadoView.class);
			}
		});
	}

	private void setearInformacionUpload()
	{
		Date fecha = new Date();
    	DateFormat formatoFecha = new SimpleDateFormat("yyyyMMddhhmmss_");
    	prefijoArchivo = formatoFecha.format(fecha);
    	ReceptorArchivos receptor = new ReceptorArchivos(Constantes.RUTA_ARCHIVOS, prefijoArchivo);
		updSubirProyecto.setReceiver(receptor);
		updSubirProyecto.setImmediateMode(false);
		updSubirProyecto.setButtonCaption(null);
	}

	private void cargarInterfazInicial()
	{
		txtNombreProyecto.setValue("");
		txtCorrector.setValue("");
		txtNota.setValue("");
	}
	

}
