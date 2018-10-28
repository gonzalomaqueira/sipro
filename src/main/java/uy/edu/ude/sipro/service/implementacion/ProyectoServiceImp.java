package uy.edu.ude.sipro.service.implementacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
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
import uy.edu.ude.sipro.utiles.HttpUtil;
import uy.edu.ude.sipro.utiles.JsonUtil;
import uy.edu.ude.sipro.utiles.SeccionTexto;

import javax.json.JsonObject;

@Service
public class ProyectoServiceImp implements ProyectoService
{
	private static final String ElasticSearch_Url_Base = "http://localhost:9200/";
	private static final String ElasticSearch_Index = "sipro_index/";
	private static final int ElasticSearch_Timeout = 3000;
	
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
	public void modificar(int id, String nombre, int anio, String carrera, int nota, String resumen, ArrayList<String> alumnos, ArrayList<String> tutorString, Set<Docente> correctores)
	{
		Proyecto proy= this.obtenerProyectoPorId(id);
		proy.setNombre(nombre);
		proy.setAnio(anio);
		proy.setCarrera(carrera);
		proy.setTutorString(tutorString);
		proy.setNota(nota);
		proy.setResumen(resumen);
		proy.setAlumnos(alumnos);
		this.cargarTutorPorString(proy);
		
		Set<Docente> docentes= docenteService.obtenerDocentes();
		Set<Docente> docentesRetorno=  new HashSet<Docente>();
		
		for(Docente d : proy.getCorrectores())
		{
			for(Docente doc : docentes)
			{
				if(d.getId()==doc.getId())
				{
					doc.getProyectosComoCorrector().remove(proy);
				}
			}
		}
		
		for(Docente d : correctores)
		{
			for(Docente doc : docentes)
			{
				if(d.getId()==doc.getId())
				{
					docentesRetorno.add(doc);
					doc.getProyectosComoCorrector().add(proy);
				}
			}
		}		
	
		proy.setCorrectores(docentesRetorno);
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
		String extension= FilenameUtils.getExtension(proyecto.getRutaArchivo());
		String parsedText = null;
		if(extension.equals("pdf"))
		{
			parsedText = devolverTextoPDF(proyecto.getRutaArchivo());
		}
		else if(extension.equals("doc") || (extension.equals("docx")))
		{
			parsedText = devolverTextoDOC(proyecto.getRutaArchivo());
		}		
        String textoOriginal[] = parsedText.split("\\r?\\n");
		return textoOriginal;
	}
	
	private String devolverTextoPDF(String rutaArchivo)
	{		
		String textoRetorno = null;
		PDDocument pdDoc = null;
		PDFTextStripper pdfStripper;
		String fileName = rutaArchivo;
		try 
		{
			pdDoc = PDDocument.load(new File(fileName));
			pdfStripper = new PDFTextStripper();
			textoRetorno = pdfStripper.getText(pdDoc);
			if (pdDoc != null)
				pdDoc.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			try
			{
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e1)
			{
				e.printStackTrace();
			}
		}
		return textoRetorno;
	}
	
	
	private String devolverTextoDOC(String rutaArchivo)
	{	
		String textoRetorno = "";
		XWPFDocument documento = null;
		FileInputStream fis = null;
		try
		{
	        File file = new File(rutaArchivo);
	        fis = new FileInputStream(file.getAbsolutePath());
	        documento = new XWPFDocument(fis);
	        List<XWPFParagraph> paragraphs = documento.getParagraphs();
	
	        for (XWPFParagraph para : paragraphs)
	        {
	        	textoRetorno = textoRetorno + para.getText() + "\r\n";
	        }
	        documento.close();
	        fis.close();
	    } 
		catch (Exception e)
		{
	        e.printStackTrace();
	        try
	        {
	        	if (documento != null)
	        		documento.close();
				if (fis != null)					
					fis.close();
			} catch (IOException e1)
	        {
				e1.printStackTrace();
			}	        
	    }
		return textoRetorno;
	}
	
	
	@Override
	public void cargarTutorPorString(Proyecto proyecto)
	{
		proyecto.setTutor(null);
		if (proyecto.getTutorString() != null && !proyecto.getTutorString().isEmpty())
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
	
	@Override
	public String buscarProyecto(String keywords) throws Exception
	{
		String jsonBody = "{\"query\":{\"match\":{\"bio\":\"" + keywords + "\"}},\"highlight\":{\"fields\":{\"bio\":{}}}}";
		StringBuilder builder = new StringBuilder();
		
		builder.append(ElasticSearch_Url_Base);
		builder.append(ElasticSearch_Index);
		builder.append("_search");
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		
		String response = HttpUtil.doPostWithJsonBody(builder.toString(), headers, jsonBody, ElasticSearch_Timeout);
		
		JsonObject jsonObject = JsonUtil.parse(response);
		
		return jsonObject.getJsonObject("hits").getJsonArray("hits").getJsonObject(0).getJsonObject("highlight").toString();
		
		
	}
}
