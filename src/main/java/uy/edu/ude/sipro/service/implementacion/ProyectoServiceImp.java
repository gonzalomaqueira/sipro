package uy.edu.ude.sipro.service.implementacion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.service.interfaces.DocenteService;
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
	@Autowired
	private DocenteService docenteService;
	
	@Transactional
	@Override
	public void agregar(String nombre, String carrera, Set<DocenteVO> correctoresVO, int nota, String rutaArchivo) 
	{
		
		Set<Docente> correctores = new HashSet<Docente>();
		Proyecto proyecto = new Proyecto(nombre, carrera, correctores, nota, rutaArchivo);
		for(Docente doc : docenteService.obtenerDocentes())
		{
			for(DocenteVO docVO : correctoresVO)
			{
				if (doc.getId() == docVO.getId())
				{
					correctores.add(doc);
					doc.getProyectosComoCorrector().add(proyecto);
					break;
				}
			}			
		}			
	    proyecto.setCorrectores(correctores);
	    proyectoDao.agregar(proyecto);
	    
	}
	
	@Transactional
	private void modificar(Proyecto proyecto)
	{
		proyectoDao.modificar(proyecto);
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
	public void modificar(int id, String nombre, int anio, String carrera, int nota, String resumen, ArrayList<String> alumnos, Docente tutor, Set<Docente> correctores)
	{
		Proyecto proy= this.obtenerProyectoPorId(id);
		proy.setNombre(nombre);
		proy.setAnio(anio);
		proy.setCarrera(carrera);
		proy.setTutor(tutor);
		proy.setCorrectores(correctores);
		proy.setNota(nota);
		proy.setResumen(resumen);
		proy.setAlumnos(alumnos);
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
		
		for (Docente doc: proyecto.getCorrectores())
		{
			doc.getProyectosComoCorrector().remove(proyecto);
		}
		proyecto.getCorrectores().removeAll(proyecto.getCorrectores());
		proyectoDao.eliminar(proyecto);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Set<Proyecto> obtenerProyectos()
	{
		return proyectoDao.obtenerProyectos();
	}
	
	@Transactional
	@Override
	public Proyecto obtenerProyectoPorId(int idProyecto)
	{
		return proyectoDao.obtenerProyectoPorId(idProyecto);
	}

	@Override
	public Set<Elemento> obtenerElementosProyecto (Proyecto proyecto, Set<Elemento> listaElementos)
	{
		boolean encontroElemento = false;
		
		Set<Elemento> listaRetorno = new HashSet<Elemento>();
		if (proyecto.getDocumentoPorSecciones() != null)
		{
			for(Elemento elemento : listaElementos)
			{
				if (!elemento.isEsCategoria())
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
	public void cargarTutorPorString(Proyecto proyecto)
	{
		if (!proyecto.getTutorString().isEmpty())
		{
			Set<Docente> docentes = docenteService.obtenerDocentes();
			for (Docente doc : docentes)
			{
				if (FuncionesTexto.ListaContieneString(proyecto.getTutorString(), doc.getApellido())
				 && FuncionesTexto.ListaContieneString(proyecto.getTutorString(), doc.getNombre()))
				{
					proyecto.setTutor(doc);
					break;
				}					
			}
		}
	}

	@Override
	@Transactional
	public void procesarProyecto(int idProyecto)
	{
		Proyecto proyecto= this.obtenerProyectoPorId(idProyecto);
		String[] textoOriginal= this.obtenerTextoOriginalProyecto(proyecto);
		proyecto.setDocumentoPorSecciones(FuncionesTexto.armarDocumentoPorSecciones(textoOriginal));
		proyecto.setAlumnos(proyecto.devolverAlumnos());
		proyecto.setTutorString(proyecto.devolverTutor());
		this.cargarTutorPorString(proyecto);
		proyecto.setResumen(FuncionesTexto.convertirArrayAStringEspacios(proyecto.devolverResumen()));
		proyecto.setElementosRelacionados(this.obtenerElementosProyecto(proyecto, elementoService.obtenerElementos()));
		proyecto.setAnio(FuncionesTexto.devolverPrimerAnioTexto(textoOriginal));
		proyecto.setEstado(EstadoProyectoEnum.PROCESADO);
		this.modificar(proyecto);
	}
}
