package uy.edu.ude.sipro.ui.vistas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.service.Fachada;
import uy.edu.ude.sipro.service.interfaces.ElementoService;


@SpringView
@SpringComponent
public class ReportesView extends ReportesViewDesign implements View{
	
	@Autowired
	Fachada fachada;
	
	@Autowired
	ElementoService elementoService;

	
	public void enter(ViewChangeEvent event)
	{		

		boton1.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{			
				Elemento elemento1 = new Elemento("Elemento 1", true, TipoElemento.TECNOLOGIA);
				Elemento elemento2 = new Elemento("Elemento 2", true, TipoElemento.METODOLOGIA_TESTING);
				Elemento elemento3 = new Elemento("Elemento 3", true, TipoElemento.TECNOLOGIA);
				Elemento elemento4 = new Elemento("Elemento 4", true, TipoElemento.METODOLOGIA_TESTING);
				Elemento elemento5 = new Elemento("Elemento 5", true, TipoElemento.METODOLOGIA_TESTING);
		
				elemento1.getElementosRelacionados().add(elemento2);
				elemento1.getElementosRelacionados().add(elemento3);
				elemento1.getElementosRelacionados().add(elemento4);
				elemento1.getElementosRelacionados().add(elemento5);
				elemento2.getElementosRelacionados().add(elemento3);
				elemento2.getElementosRelacionados().add(elemento4);
				elemento2.getElementosRelacionados().add(elemento1);
				elemento3.getElementosRelacionados().add(elemento4);
				elemento3.getElementosRelacionados().add(elemento1);
				elemento4.getElementosRelacionados().add(elemento1);
				elemento5.getElementosRelacionados().add(elemento2);
				
		
				elementoService.agregar(elemento1);
			}
		});
//		Elemento elemento1 = new Elemento("Elemento 1", true, TipoElemento.TECNOLOGIA);
//		Elemento elemento2 = new Elemento("Elemento 2", true, TipoElemento.METODOLOGIA_TESTING);
//		Elemento elemento3 = new Elemento("Elemento 3", true, TipoElemento.TECNOLOGIA);
//		Elemento elemento4 = new Elemento("Elemento 4", true, TipoElemento.METODOLOGIA_TESTING);
//		Elemento elemento5 = new Elemento("Elemento 5", true, TipoElemento.METODOLOGIA_TESTING);
//
//		elemento1.getElementosRelacionados().add(elemento2);
//		elemento1.getElementosRelacionados().add(elemento3);
//		elemento1.getElementosRelacionados().add(elemento4);
//		elemento1.getElementosRelacionados().add(elemento5);
//		elemento2.getElementosRelacionados().add(elemento3);
//		elemento2.getElementosRelacionados().add(elemento4);
//		elemento2.getElementosRelacionados().add(elemento1);
//		elemento3.getElementosRelacionados().add(elemento4);
//		elemento3.getElementosRelacionados().add(elemento1);
//		elemento4.getElementosRelacionados().add(elemento1);
//		elemento5.getElementosRelacionados().add(elemento2);
//		
//
//		elementoService.agregar(elemento1);
		
//		List<Elemento> ListaElementos = elementoService.obtenerElementos();
		
//		Elemento elemento1 = elementoService.obtenerElementoPorId(1);
//		Elemento elemento2 = elementoService.obtenerElementoPorId(2);
//		Elemento elemento3 = elementoService.obtenerElementoPorId(3);
//		Elemento elemento4 = elementoService.obtenerElementoPorId(4);
//		Elemento elemento5 = elementoService.obtenerElementoPorId(5);
//		
		//elemento5.getElementosRelacionados().add(elemento3);
		//elemento5.getElementosRelacionados().add(elemento4);
		
		
		
//		List<Elemento> listaNueva = new ArrayList<Elemento>();          
//		listaNueva.add(elemento1);
//		listaNueva.add(elemento2);
//		listaNueva.add(elemento4);
		
//		elemento5.setElementosRelacionados(listaNueva);
//		Elemento elemento1=null;
//		Elemento elemento2=null;
//		Elemento elemento3=null;
//		Elemento elemento4=null;
//		Elemento elemento5=null;
//		
//
//		for(Elemento elem : ListaElementos)
//		{
//			if(elem.getId()==1)
//				elemento1=elem;
//			if(elem.getId()==2)
//				elemento2=elem;
//			if(elem.getId()==3)
//				elemento3=elem;
//			if(elem.getId()==4)
//				elemento4=elem;
//			if(elem.getId()==5)
//				elemento5=elem;
//		}
//		
//		elemento5.getElementosRelacionados().add(elemento1);
//		elemento5.getElementosRelacionados().add(elemento2);
//		elemento5.getElementosRelacionados().add(elemento3);
//		elemento5.getElementosRelacionados().add(elemento4);
//		elementoService.agregar(elemento5);
		

//		elementoService.modificar(elemento5.getId(), "pepe", true, TipoElemento.METODOLOGIA_TESTING, listaNueva, new ArrayList<Sinonimo>());
	

		
//		
//		elemento2.getElementosOrigen().remove(elemento2.getElementosOrigen().get(0));
//		elemento1.getElementosRelacionados().remove(elemento1.getElementosRelacionados().get(0));
		
//		elementoService.eliminar(elemento1.getId());
//	
//		elemento1.getElementosOrigen().add(elemento3);


//		elementoService.agregar(elemento2);

//		
//		elemento1.getElementosRelacionados().add(elemento2);
//		elemento1.getElementosRelacionados().add(elemento3);
//		elemento1.getElementosRelacionados().add(elemento4);
//		elemento1.getElementosRelacionados().add(elemento5);
//		
//		elemento2.getElementosRelacionados().add(elemento3);
//		elemento2.getElementosRelacionados().add(elemento4);
//		elemento2.getElementosRelacionados().add(elemento5);
//		
//		elemento3.getElementosRelacionados().add(elemento4);
//		elemento3.getElementosRelacionados().add(elemento5);
//		
		

//		elementoService.agregar(elemento1);
//		elementoService.agregar(elemento2);
//		elementoService.agregar(elemento3);
		
		
	//	@SuppressWarnings("unused")
		//List<Elemento> ListaElementos = elementoService.obtenerElementos();	 
		

//		Elemento elem = elementoService.obtenerElementoPorId(1);
		
//		List<Elemento> relacionados = elem.getElementoRelacionados();
//		List<Elemento> origenes = elem.getElementosOrigen();
		
		@SuppressWarnings("unused")
		PDDocument pdDoc = null;
//		PDFTextStripper pdfStripper;
//
//		String parsedText;
//		String fileName = "C:\\temp\\Resumenes\\JDBC1.pdf";
//		try {
//
////			pdDoc = PDDocument.load(new File(fileName));
////			pdfStripper = new PDFTextStripper();
////			parsedText = pdfStripper.getText(pdDoc);
////	        String textoOriginal[] = parsedText.split("\\r?\\n");
////	        
////	        Proyecto proyecto= new Proyecto();
////	        proyecto.setDocumentoPorSecciones(fachada.armarDocumentoPorSecciones(textoOriginal));
////	        System.out.println(parsedText);
////	        
////	        
////	        List<Tecnologia> vTecnologiasDelProyecto = fachada.obtenerTecnologiasProyecto(proyecto);	  	        
////	        for(Tecnologia tecnologia : vTecnologiasDelProyecto)
////			{
////	        	System.out.println(tecnologia.toString());
////			}
////	        
////	        List<ModeloProceso> vModelosProcesoDelProyecto = fachada.obtenerModelosProcesoProyecto(proyecto);	  	        
////	        for(ModeloProceso modelo : vModelosProcesoDelProyecto)
////			{
////	        	System.out.println(modelo.toString());
////			}
////	        
////
////	        List<MetodologiaTesting> vMetodologiaTestingDelProyecto = fachada.obtenerMetodologiasTestingProyecto(proyecto);  	        	        
////	        for(MetodologiaTesting metodologia : vMetodologiaTestingDelProyecto)
////			{
////	        	System.out.println(metodologia.toString());
////			}
//	        
//	        /*
//	        for(String linea : proyecto.devolverResumen())
//	        {	        	
//	        	System.out.println(linea);
//	        }
//	        
//	        for(SeccionTexto sec : proyecto.getDocumentoPorSecciones())
//	        {	        	
//	        	sec.listarSeccion();
//	        }
//	        
//	        /*
//	        for(String linea : proyecto.devolverAlumnos())
//	        {
//	        	System.out.println(linea);
//	        }*/
//	        
//	        System.out.println("--------------------------------------------");
//	        System.out.println("--------------------------------------------");
//	        /*
//	        for(String linea : proyecto.devolverTutor())
//	        {
//	        	System.out.println(linea);
//	        }*/
//	        
//	        
//		} catch (Exception e) {
//			e.printStackTrace();
//			try {
//				if (pdDoc != null)
//					pdDoc.close();
//			} catch (Exception e1) {
//				e.printStackTrace();
//			}
//		}
//		link.setCaption("Hola!!!!!");
	}
	private boolean contieneElemento(Elemento elem1, Elemento elem2)
	{
		for (Elemento e1 : elem1.getElementosRelacionados())
		{
			if (e1.getId() == elem2.getId())
				return true;
		}			
		return false;
	}

}
