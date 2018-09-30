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
	public void agregar(String nombre, String carrera, String corrector, int nota, String rutaArchivo) 
	{
	   Proyecto proyecto = new Proyecto(nombre, carrera, corrector, nota, rutaArchivo);
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
	public void modificar(int id, String nombre, int anio, String carrera, String corrector, int nota, String resumen, ArrayList<String> alumnos, ArrayList<String> tutor)
	{
		Proyecto proy= this.obtenerProyectoPorId(id);
		proy.setNombre(nombre);
		proy.setAnio(anio);
		proy.setCarrera(carrera);
		proy.setCorrector(corrector);
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
	@Transactional
	public void procesarProyecto(int idProyecto)
	{
		Proyecto proyecto= this.obtenerProyectoPorId(idProyecto);
		String[] textoOriginal= this.obtenerTextoOriginalProyecto(proyecto);
		proyecto.setDocumentoPorSecciones(FuncionesTexto.armarDocumentoPorSecciones(textoOriginal));
		proyecto.setAlumnos(proyecto.devolverAlumnos());
		proyecto.setTutor(proyecto.devolverTutor());
		proyecto.setResumen(FuncionesTexto.convertirArrayAStringEspacios(proyecto.devolverResumen()));
		proyecto.setElementosRelacionados(this.obtenerElementosProyecto(proyecto, elementoService.obtenerElementos()));
		proyecto.setAnio(FuncionesTexto.devolverPrimerAnioTexto(textoOriginal));
		proyecto.setEstado(EstadoProyectoEnum.PROCESADO);
		this.modificar(proyecto);
	}
}
