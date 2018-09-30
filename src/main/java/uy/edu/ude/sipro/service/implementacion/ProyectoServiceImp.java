package uy.edu.ude.sipro.service.implementacion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.dao.interfaces.ProyectoDao;
import uy.edu.ude.sipro.utiles.FuncionesTexto;
import uy.edu.ude.sipro.utiles.SeccionTexto;

@Service
public class ProyectoServiceImp implements ProyectoService
{
	@Autowired
	private ProyectoDao proyectoDao;
	
	@Autowired
	private ElementoService elementoService;
	
	@Transactional
	@Override
	public void agregar(String nombre, String correctores, int nota, String rutaArchivo) 
	{
	   Proyecto proyecto = new Proyecto(nombre, nota, rutaArchivo);
	   proyectoDao.agregar(proyecto);
	}

	@Transactional
	@Override
	public void modificar(int id, String nombre, int anio, String carrera, int nota, String rutaArchivo) 
	{
		Proyecto proy= this.obtenerProyectoPorId(id);
		proy.setNombre(nombre);
		proy.setAnio(anio);
		proy.setCarrera(carrera);
		proy.setNota(nota);
		proyectoDao.modificar(proy);
	}
	
	@Transactional
	@Override
	public void modificar(int id, String nombre, int anio, String carrera, int nota, String resumen, ArrayList<String> alumnos, ArrayList<String> tutor)
	{
		Proyecto proy= this.obtenerProyectoPorId(id);
		proy.setNombre(nombre);
		proy.setAnio(anio);
		proy.setCarrera(carrera);
		proy.setNota(nota);
		proy.setResumen(resumen);
		proy.setAlumnos(alumnos);
		proy.setTutor(tutor);
		proyectoDao.modificar(proy);
	}
		
	@Transactional
	@Override
	public void eliminar(int id) 
	{
		Proyecto proyecto = proyectoDao.obtenerProyectoPorId(id);
		for (Elemento elem: proyecto.getElementosRelacionados())
		{
			elem.getProyectos().remove(proyecto);
		}
		proyecto.getElementosRelacionados().removeAll(proyecto.getElementosRelacionados());
		proyectoDao.eliminar(proyecto);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Proyecto> obtenerProyectos()
	{
		return proyectoDao.obtenerProyectos();
	}
	
	@Transactional
	@Override
	public Proyecto obtenerProyectoPorId(int idProyecto)
	{
		return proyectoDao.obtenerProyectoPorId(idProyecto);
//		Proyecto proy = proyectoDao.obtenerProyectoPorId(idProyecto);
//		if (proy.getTecnologias() == null || proy.getTecnologias().isEmpty())
//		{
//			proy.setTecnologias(new ArrayList<Elemento>());
//		}
//		if (proy.getModeloProceso() == null || proy.getModeloProceso().isEmpty())
//		{
//			proy.setModeloProceso(new ArrayList<Elemento>());
//		}
//		if (proy.getMetodologiaTesting() == null || proy.getMetodologiaTesting().isEmpty())
//		{
//			proy.setMetodologiaTesting(new ArrayList<Elemento>());
//		}
//		for(Elemento tec : proy.getTecnologias())
//		{
//			if (tec.getSinonimos() == null || tec.getSinonimos().isEmpty())
//				tec.setSinonimos(new ArrayList<SinonimoTecnologia>());
//		}
//		
//		return proy;
	}

	@Override
	public List<Elemento> obtenerElementosProyecto (Proyecto proyecto, List<Elemento> listaElementos)
	{
		boolean encontroElemento = false;
		
		List<Elemento> listaRetorno = new ArrayList<Elemento>();
		if (proyecto.getDocumentoPorSecciones() != null)
		{
			for(Elemento elemento : listaElementos)
			{
				for(SeccionTexto seccion : proyecto.getDocumentoPorSecciones())
				{
					if (encontroElemento)
					{
						encontroElemento = false;
						break;
					}
					if(FuncionesTexto.seccionContieneTexto(seccion, elemento.getNombre()))
					{
						listaRetorno.add(elemento);
						break;
					}
					else
					{
						for (Sinonimo sinonimo: elemento.getSinonimos())
						{
							if(FuncionesTexto.seccionContieneTexto(seccion, sinonimo.getNombre()))
							{
								listaRetorno.add(elemento);
								encontroElemento = true;
								break;
							}
						}
					}
				}	
			}
		}
		return listaRetorno;
	}
	
	@Override
	public String[] obtenerTextoOriginalProyecto(Proyecto proyecto)
	{
		PDDocument pdDoc = null;
		PDFTextStripper pdfStripper;
		String parsedText = null;
		String fileName = proyecto.getRutaArchivo();
		try 
		{
			pdDoc = PDDocument.load(new File(fileName));
			pdfStripper = new PDFTextStripper();
			parsedText = pdfStripper.getText(pdDoc);
			if (pdDoc != null)
				pdDoc.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			try {
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}
		
        String textoOriginal[] = parsedText.split("\\r?\\n");
		return textoOriginal;
	}
	
	@Override
	public List<SeccionTexto> armarDocumentoPorSecciones(String textoOriginal[])
	{
		List<SeccionTexto> documentoPorSecciones = new ArrayList<SeccionTexto>();
        SeccionTexto seccion = null;
        ArrayList<String> contenido = new ArrayList<String>();
        boolean encontreTitulo = false;
		
        documentoPorSecciones.add(armarSeccionAlumnos(textoOriginal));
        documentoPorSecciones.add(armarSeccionTutor(textoOriginal));
         
        List<String> Textolista = new ArrayList<String>(Arrays.asList(textoOriginal));
        
        for (String linea : textoOriginal)
        {
        	if (!FuncionesTexto.esTituloResumen(linea))
        	{
        		Textolista.remove(0);
        	}
        	else
        		break;
        }
        
		for (String linea : Textolista)
        {
    	    if (FuncionesTexto.esTitulo(linea))
    	    {
    	    	encontreTitulo = true;
    	    	if (seccion != null)
    	    	{
    	    		seccion.setContenido(contenido);
    	    		documentoPorSecciones.add(seccion);
    	    	}
    	    	seccion = new SeccionTexto();
    	    	contenido = new ArrayList<String>();
    	    	seccion.setTitulo(linea);
			}
    	    else
    	    {
    	    	if (encontreTitulo && contenido != null)
    	    	{
    	    		contenido.add(linea);
    	    	}        	    	
    	    }
        }
		if (seccion != null)
		{
			seccion.setContenido(contenido);
			documentoPorSecciones.add(seccion);
		}
		
		return documentoPorSecciones;
	}
	
	@Override
	public SeccionTexto armarSeccionAlumnos(String texto[])
	{
        SeccionTexto seccion = null;
        ArrayList<String> contenido = new ArrayList<String>();
        boolean encontreTituloAlumno = false;
        
		for (String linea : texto)
        {
    	    if (FuncionesTexto.esTituloTutor(linea))
    	    	break;
    	    
			if (FuncionesTexto.esTituloAlumnos(linea))
    	    {
    	    	encontreTituloAlumno = true;
    	    	if (seccion != null)
    	    	{
    	    		seccion.setContenido(contenido);
    	    	}
    	    	seccion = new SeccionTexto();
    	    	contenido = new ArrayList<String>();
    	    	seccion.setTitulo(linea);
			}
    	    else
    	    {
    	    	if (encontreTituloAlumno && !FuncionesTexto.esTituloTutor(linea))
    	    	{
    	    		contenido.add(linea);
    	    	}        	    	
    	    }
        }
		if (seccion != null)
		{
			seccion.setContenido(contenido);
		}
		return seccion;
	}
	
	@Override
	public SeccionTexto armarSeccionTutor(String texto[])
	{
        SeccionTexto seccion = null;
        ArrayList<String> contenido = new ArrayList<String>();
        boolean encontreTituloAlumno = false;
        
		for (String linea : texto)
        {
    	    if (FuncionesTexto.esTituloResumen(linea))
    	    	break;
    	    
			if (FuncionesTexto.esTituloTutor(linea))
    	    {
    	    	encontreTituloAlumno = true;
    	    	if (seccion != null)
    	    	{
    	    		seccion.setContenido(contenido);
    	    	}
    	    	seccion = new SeccionTexto();
    	    	contenido = new ArrayList<String>();
    	    	seccion.setTitulo(linea);
			}
    	    else
    	    {
    	    	if (encontreTituloAlumno && !FuncionesTexto.esTituloResumen(linea))
    	    	{
    	    		contenido.add(linea);
    	    	}        	    	
    	    }
        }
		if (seccion != null)
		{
			seccion.setContenido(contenido);
		}
		return seccion;
	}
	
	@Override
	public void procesarProyecto(int idProyecto)
	{
		Proyecto proyecto= this.obtenerProyectoPorId(idProyecto);
		String[] textoOriginal= this.obtenerTextoOriginalProyecto(proyecto);
		proyecto.setDocumentoPorSecciones(this.armarDocumentoPorSecciones(textoOriginal));
		proyecto.setAlumnos(proyecto.devolverAlumnos());
		proyecto.setTutor(proyecto.devolverTutor());
		proyecto.setResumen(FuncionesTexto.convertirArrayAStringEspacios(proyecto.devolverResumen()));
		//proyecto.setElementosRelacionados(this.obtenerElementosProyecto(proyecto, elementoService.obtenerElementos()));
		proyecto.setEstado(EstadoProyectoEnum.PROCESADO);
		this.modificarProyecto(proyecto);
	}
	
	@Transactional
	private void modificarProyecto(Proyecto proyecto)
	{
		proyectoDao.modificar(proyecto);
	}
}
