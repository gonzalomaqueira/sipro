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

import uy.edu.ude.sipro.navegacion.NavigationManager;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.utiles.Constantes;
import uy.edu.ude.sipro.utiles.ReceptorArchivos;
import uy.edu.ude.sipro.valueObjects.CorrectorVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;


@SpringView
@SpringComponent
public class ProyectoNuevoView extends ProyectoNuevoViewDesign implements View
{
	@Autowired
	private Fachada fachada;

    private final NavigationManager navigationManager;
    
    private String nombreArchivo;
    private String prefijoArchivo;
    private List<CorrectorVO> listaCorrectores;
    private CorrectorVO correctorSeleccionado;
    
    @Autowired
    public ProyectoNuevoView (NavigationManager navigationManager)
    {
    	this.navigationManager = navigationManager;
    	this.listaCorrectores = new ArrayList<CorrectorVO>();
    }
	
	public void enter(ViewChangeEvent event)
	{
		cargarInterfazInicial();
		cargarCmbCorrectores();
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
									 listaCorrectores,
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
		
		cmbCorrectores.addValueChangeListener(evt -> 
		{
		    if (!evt.getSource().isEmpty()) 
		    {
		    	correctorSeleccionado= new CorrectorVO();
		    	correctorSeleccionado.setId(evt.getValue().getId());
		    	correctorSeleccionado.setNombre(evt.getValue().getNombre());
		    	btnAgregarCorrector.setEnabled(true);
		    }
		});
		
		btnAgregarCorrector.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{	
				if(correctorSeleccionado.getId()!=0)//ver esto, no detecta null, ver si en base nunca genera un elemento id =0!!!!!!!
				{
					listaCorrectores.add(correctorSeleccionado);
					grdCorrectores.setItems(listaCorrectores);
					cargarCmbCorrectores();
				}
			}

		});
		
		grdCorrectores.addSelectionListener(evt -> 
		{
			SingleSelectionModel<CorrectorVO> singleSelect = (SingleSelectionModel<CorrectorVO>) grdCorrectores.getSelectionModel();
			singleSelect.setDeselectAllowed(false);
			try
			{
				if (singleSelect.getSelectedItem() != null)
				{
					correctorSeleccionado = singleSelect.getSelectedItem().get();
					btnAgregarCorrector.setEnabled(false);
					btnEliminarCorrector.setVisible(true);
				}
			}
			catch (Exception e)
			{
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
		txtNota.setValue("");
	}
	
	private void cargarCmbCorrectores()
	{		
		if(correctorSeleccionado != null)
		{
			List<CorrectorVO> correctores= fachada.obtenerCorrectores();
			List<CorrectorVO> correctoresAux = new ArrayList<CorrectorVO>(correctores);
			for(CorrectorVO cor : correctoresAux)
			{				
				for(CorrectorVO corAux : listaCorrectores)
				{
					if(cor.getId()==corAux.getId())
					{
						correctores.remove(cor);
					    break;
					}
				}
			}
			cmbCorrectores.setItems(correctores);
			cmbCorrectores.setItemCaptionGenerator(CorrectorVO::getNombre);
			cmbCorrectores.setValue(null);
			btnAgregarCorrector.setEnabled(false);
		}
		else
		{
			cmbCorrectores.setItems(fachada.obtenerCorrectores());
			cmbCorrectores.setItemCaptionGenerator(CorrectorVO::getNombre);
		}

	}
}
