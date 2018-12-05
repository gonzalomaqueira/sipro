package uy.edu.ude.sipro.busquedas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.utiles.Constantes;
import uy.edu.ude.sipro.utiles.FuncionesTexto;
import uy.edu.ude.sipro.utiles.HttpUtil;
import uy.edu.ude.sipro.utiles.JsonUtil;

@Service
public class BusquedaServiceImp implements BusquedaService {

	@Autowired
	private ElementoService elementoService;
	
	@Autowired
	private ProyectoService proyectoService;		
	
	@Override
	public boolean altaProyectoES(Proyecto proyecto, String[] textoOriginal) throws Exception
	{
		String JsonArray = JsonUtil.devolverJsonArray(proyecto.getListaStringElementos());
		
		String nota = proyecto.getNota() < 10 ? "0" + proyecto.getNota() : String.valueOf(proyecto.getNota());
		textoOriginal = FuncionesTexto.eliminarBibliografia(textoOriginal);
		
		String jsonBody = "{\"id_ude\":\"" + proyecto.getCodigoUde()
						+ "\",\"titulo\":\"" + proyecto.getTitulo()
						+ "\",\"anio\":\"" + proyecto.getAnio()
						+ "\",\"nota\":\"" + nota
						+ "\",\"tutor\":\"" + proyecto.getTutorString()
						+ "\",\"resumen\":\"" + proyecto.getResumen()
						+ "\",\"contenido\":\"" + FuncionesTexto.limpiarTexto(textoOriginal)
						+ "\"}";
		
		System.out.println(jsonBody);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		builder.append("proyectos/");
		builder.append(Integer.toString(proyecto.getId()));
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		jsonBody = FuncionesTexto.limpiarTextoFull(jsonBody);
		
		String response = HttpUtil.doPutWithJsonBody(builder.toString(), headers, jsonBody, Constantes.ElasticSearch_Timeout);
		
		JsonObject jsonObject = JsonUtil.parse(response);
		
		return jsonObject.getJsonString("result").toString().equals("\"created\"") || 
			   jsonObject.getJsonString("result").toString().equals("\"updated\"");
	}
	
	@Override
	public boolean bajaProyectoES(int idProyecto ) throws Exception
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		builder.append("proyectos/");
		builder.append(idProyecto);
		String response = HttpUtil.doDelete(builder.toString(), Constantes.ElasticSearch_Timeout);
		
		JsonObject jsonObject = JsonUtil.parse(response);
		
		return jsonObject.getJsonString("result").toString().equals("\"deleted\"");
	}
	
	@Override
	public ArrayList<ResultadoBusqueda> realizarBusquedaES(String busqueda, DatosFiltro datosFiltro) throws Exception
	{
		String response;
		StringBuilder builder = new StringBuilder();
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		String jsonBody;
		boolean esBusquedaDirecta = false;
		
		Proyecto proyectoPorCodigo = proyectoService.buscarProyecto(busqueda);
		ArrayList<Elemento> elementosPrimarios = this.obtenerElementoString(busqueda);
		ArrayList<Elemento> elementosRelacionados = this.obtenerElementosRelacionadosBusqueda(elementosPrimarios);

		if(proyectoPorCodigo != null)
		{
			builder.append("proyectos/");
			builder.append(proyectoPorCodigo.getId());
			response = HttpUtil.doGet(builder.toString(), Constantes.ElasticSearch_Timeout);
			esBusquedaDirecta = true;
		}
		else
		{
			if (busqueda.equals(""))
			{
				jsonBody = "{\"_source\":{\"excludes\":[\"contenido\"]},\"query\":{\"bool\":{"
							+ this.cargarFiltros(datosFiltro)
							+"\"should\": [{\"match_all\":{}}]}},\"sort\":{\"anio\":{\"order\":\"desc\"}}, \"size\": 1000}";
			}
			else
			{
				if (elementosPrimarios != null && !elementosPrimarios.isEmpty())
				{
					jsonBody = "{\"_source\":{\"excludes\":[\"contenido\"]},\"query\":{\"bool\":{"
								+ this.cargarFiltros(datosFiltro) + "\"should\": [";
					for(Elemento elem : elementosPrimarios)
					{
						jsonBody = jsonBody + "{\"match_phrase\": {\"contenido\": { \"query\":\"" + elem.getNombre() + "\", \"boost\": 10 }}},";
					}
					if (elementosRelacionados != null && !elementosRelacionados.isEmpty())
					{
						for(Elemento elem : elementosRelacionados)
						{
							jsonBody = jsonBody + "{\"match_phrase\": {\"contenido\": { \"query\":\"" + elem.getNombre() + "\", \"boost\": 1 }}},";
						}
					}
					jsonBody = jsonBody.substring(0,jsonBody.length() - 1);
					jsonBody = jsonBody + "],\"minimum_should_match\" : 1}},\"highlight\":{\"fields\":{\"contenido\":{}}}, \"size\": 1000}";
				}
				else
				{
					jsonBody = "{\"_source\":{\"excludes\":[\"contenido\"]},\"query\":{\"bool\":{"
							+ this.cargarFiltros(datosFiltro)
							+"\"should\": [{\"match\":{\"contenido\":\"" + busqueda + "\"}}],\"minimum_should_match\" : 1}},\"highlight\":{\"fields\":{\"contenido\":{}}}, \"size\": 1000}";		
				}
			}
			
			builder.append("_search");			
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json");
			
			response = HttpUtil.doPostWithJsonBody(builder.toString(), headers, jsonBody, Constantes.ElasticSearch_Timeout);			
		}
		return obtenerResultadoDesdeJson(response, esBusquedaDirecta);
		
	}

	private String cargarFiltros(DatosFiltro datosFiltro)
	{
		String filtros = "";			
		if(datosFiltro.isFiltroHabilitado())
		{
			String filtro = "\"filter\": ["; 
			String notaIni = datosFiltro.getNotaIni() < 10 ? "0" + datosFiltro.getNotaIni() : String.valueOf(datosFiltro.getNotaIni());
			String notaFin = datosFiltro.getNotaFin() < 10 ? "0" + datosFiltro.getNotaFin() : String.valueOf(datosFiltro.getNotaFin());
			
			filtro= filtro + "{ \"range\": { \"anio\": { \"gte\": \"" + datosFiltro.getAnioIni() + "\", \"lte\": \"" + datosFiltro.getAnioFin()+ "\" }}},";
			filtro= filtro + "{ \"range\": { \"nota\": { \"gte\": \"" + notaIni + "\", \"lte\": \"" + notaFin+ "\" }}}";
			filtro = filtro + "],";

			String must = "";
			if(!datosFiltro.getTutor().isEmpty() || !datosFiltro.getTutor().equals(""))
			{
				must = "\"must\": [";
				must = must + "{ \"match\": { \"tutor\":\"" + datosFiltro.getTutor() + "\" }}";	
				must = must + "],";
			}
			filtros = filtro + must;
		}
		return filtros;
	}

	private ArrayList<ResultadoBusqueda> obtenerResultadoDesdeJson(String json, boolean esBusquedaDirecta) throws Exception
	{
		ArrayList<ResultadoBusqueda> resultado= new ArrayList<ResultadoBusqueda>();
		JsonObject jsonObject = JsonUtil.parse(json);
		ResultadoBusqueda resultadoBusqueda;
		
		if (esBusquedaDirecta)
		{
			if (jsonObject.getBoolean("found"))
			{
				resultadoBusqueda= new ResultadoBusqueda();
				
				String id = jsonObject.getString("_id");
				String score = "1.0";
				String titulo = jsonObject.getJsonObject("_source").getString("titulo");
				String codigoUde = jsonObject.getJsonObject("_source").getString("id_ude");
				String anio = jsonObject.getJsonObject("_source").getString("anio");
				String nota = jsonObject.getJsonObject("_source").getString("nota");
				String resumen = jsonObject.getJsonObject("_source").getString("resumen");
				
				resultadoBusqueda.setIdProyecto(Integer.parseInt(id));
				resultadoBusqueda.setScore(Float.parseFloat(score));
				resultadoBusqueda.setTituloProyecto(titulo);
				resultadoBusqueda.setCodigoUde(codigoUde);
				resultadoBusqueda.setAnio(Integer.parseInt(anio));
				resultadoBusqueda.setNota(Integer.parseInt(nota));
				resultadoBusqueda.setAbstractProyecto(resumen);
				
				resultado.add(resultadoBusqueda);
				
			}
		}
		else
		{
			Iterator<JsonValue> iterador = jsonObject.getJsonObject("hits").getJsonArray("hits").iterator();	
			while (iterador.hasNext())
			{	
				JsonValue jsonValue = iterador.next();
	
				resultadoBusqueda= new ResultadoBusqueda();
				
				String id = jsonValue.asJsonObject().getString("_id");
				String score = "0.0";
				if (!jsonValue.asJsonObject().isNull("_score"))
				{
					score = jsonValue.asJsonObject().getJsonNumber("_score").toString();
				}				
				String titulo = jsonValue.asJsonObject().getJsonObject("_source").getString("titulo");
				String codigoUde = jsonValue.asJsonObject().getJsonObject("_source").getString("id_ude");
				String anio = jsonValue.asJsonObject().getJsonObject("_source").getString("anio");
				String nota = jsonValue.asJsonObject().getJsonObject("_source").getString("nota");
				String resumen = jsonValue.asJsonObject().getJsonObject("_source").getString("resumen");
				
				if (jsonValue.asJsonObject().getJsonObject("highlight") != null)
				{
					Iterator<JsonValue> iterHighlight;
					iterHighlight = jsonValue.asJsonObject().getJsonObject("highlight").getJsonArray("contenido").iterator();
	
					ArrayList<String> resultadosHighlight = new ArrayList<>();
					while (iterHighlight.hasNext())
					{
						JsonValue jsonValueHighlight = iterHighlight.next();
						String highlight = jsonValueHighlight.toString();
						resultadosHighlight.add(highlight);
					}
					resultadoBusqueda.setHighlight(resultadosHighlight);
				}			
				
				resultadoBusqueda.setIdProyecto(Integer.parseInt(id));
				resultadoBusqueda.setScore(Float.parseFloat(score));
				resultadoBusqueda.setTituloProyecto(titulo);
				resultadoBusqueda.setCodigoUde(codigoUde);
				resultadoBusqueda.setAnio(Integer.parseInt(anio));
				resultadoBusqueda.setNota(Integer.parseInt(nota));
				resultadoBusqueda.setAbstractProyecto(resumen);
				
				resultado.add(resultadoBusqueda);
			}
		}
		return resultado;
	}
	
	@Override
	public boolean actualizarSinonimosElemntosES(ArrayList<Elemento> elementos) throws Exception
	{
		boolean retorno = false;
		try
		{
			abrirCerrarIndiceES(false);
			
			String jsonBody = "{\"analysis\": {" + 
								"    \"filter\": {" + 
								"      \"filtro_sinonimos\": {" + 
								"        \"type\": \"synonym\"," + 
								"        \"synonyms\": "+  JsonUtil.devolverJsonArrayElementosSinonimos(elementos) +
								"      }" + 
								"    }" + 
								"  }" + 
								"}";
			
			StringBuilder builder = new StringBuilder();
			
			builder.append(Constantes.ElasticSearch_Url_Base);
			builder.append(Constantes.ElasticSearch_Index);
			builder.append("_settings");
			
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json");
			
			String response = HttpUtil.doPutWithJsonBody(builder.toString(), headers, jsonBody, Constantes.ElasticSearch_Timeout);
			
			JsonObject jsonObject = JsonUtil.parse(response);
			
			retorno = jsonObject.getBoolean("acknowledged");
		}
		finally
		{
			abrirCerrarIndiceES(true);
		}
		
		return retorno;
	}
	
	
	@Override
	public boolean abrirCerrarIndiceES(boolean abrirConexion) throws Exception
	{	
		StringBuilder builder = new StringBuilder();
		
		String accion = abrirConexion ? "_open" : "_close";
		
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		builder.append(accion);
		
		String response = HttpUtil.doPost(builder.toString(), Constantes.ElasticSearch_Timeout);
		
		JsonObject jsonObject = JsonUtil.parse(response);
		
		return jsonObject.getBoolean("acknowledged");
	}
	
	
	private ArrayList<Elemento> obtenerElementoString(String busqueda) {
		
		busqueda= busqueda.toLowerCase().trim();
		ArrayList<Elemento> retorno= new ArrayList<Elemento>();
		HashSet<Elemento> elementos= (HashSet<Elemento>) elementoService.obtenerElementos();
		for(Elemento elem : elementos)
		{
			if (busqueda.contains(elem.getNombre().toLowerCase().trim()))
			{
				retorno.add(elem);
			}
			else
			{
				for(Sinonimo sin : elem.getSinonimos())
				{
					if(busqueda.contains(sin.getNombre().toLowerCase().trim()))
					{
						retorno.add(elem);
						break;
					}
				}
			}
		
		}
		
		return retorno;
	}
	
	private ArrayList<Elemento> obtenerElementosRelacionadosBusqueda(ArrayList<Elemento> elementosPrimarios)
	{
		ArrayList<Elemento> retorno = new ArrayList<Elemento>();
		for(Elemento elem : elementosPrimarios)
		{
			retorno.addAll((ArrayList<Elemento>) elem.getElementosRelacionados()
					.stream()
					.filter(x -> !x.isEsCategoria())
					.collect(Collectors.toList()));
		}
		return retorno;
	}
	
	public ArrayList<Integer> obtenerListaProyectosES() throws Exception
	{
		ArrayList<Integer> listaRetorno = new ArrayList<>();

		String jsonBody = "{\"_source\":false,\"query\":{\"match_all\":{}}, \"size\": 1000}";		
		
		StringBuilder builder = new StringBuilder();
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		builder.append("proyectos/_search");
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		Thread.sleep(1000);
		
		String response = HttpUtil.doPostWithJsonBody(builder.toString(), headers, jsonBody, Constantes.ElasticSearch_Timeout);

		JsonObject jsonObject = JsonUtil.parse(response);
		
		if (jsonObject.getJsonObject("hits") != null)
		{
			Iterator<JsonValue> iterador = jsonObject.getJsonObject("hits").getJsonArray("hits").iterator();	
			while (iterador.hasNext())
			{	
				JsonValue jsonValue = iterador.next();
				String id = jsonValue.asJsonObject().getString("_id");
				
				listaRetorno.add(Integer.parseInt(id));
			}
		}
	
		return listaRetorno;
	}
	
	public void creacionIncideES() throws Exception
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		
		String response = HttpUtil.doGet(builder.toString(), Constantes.ElasticSearch_Timeout);
		JsonObject jsonObject = JsonUtil.parse(response);
		
		if (jsonObject.getJsonObject("error") != null)
		{
			String jsonBody = "{\"settings\":{\"index\":{\"analysis\":{\"filter\":{\"filtro_sinonimos\":{"
					+ "\"type\":\"synonym\",\"synonyms\":[]}},\"analyzer\":{\"sinonimos_analyzer\":{"
					+ "\"tokenizer\":\"standard\",\"filter\":[\"lowercase\",\"filtro_sinonimos\"]}}}}},"
					+ "\"mappings\":{\"proyectos\":{\"properties\":{\"contenido\":{\"type\":\"text\",\"analyzer\":"
					+ "\"sinonimos_analyzer\"},\"anio\":{\"type\":\"text\",\"fielddata\":true},\"nota\":{"
					+ "\"type\":\"text\",\"fielddata\":true},\"tutor\":{\"type\":\"text\",\"fielddata\":true}}}}}";
			
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json");
			
			response = HttpUtil.doPutWithJsonBody(builder.toString(), headers, jsonBody, Constantes.ElasticSearch_Timeout);
			
			jsonObject = JsonUtil.parse(response);
			
			if (!jsonObject.getBoolean("acknowledged"))
			{
				throw new Exception("Problema la crear el índice");
			}
		}
	}
}
