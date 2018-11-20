package uy.edu.ude.sipro.busquedas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.utiles.Constantes;
import uy.edu.ude.sipro.utiles.FuncionesTexto;
import uy.edu.ude.sipro.utiles.HttpUtil;
import uy.edu.ude.sipro.utiles.JsonUtil;

@Service
public class BusquedaServiceImp implements BusquedaService {

	@Autowired
	private ElementoService elementoService;
	
	@Override
	public ArrayList<Elemento> obtenerElementoString(String busqueda) {
		
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
	
	public boolean altaProyectoES(Proyecto proyecto , String[] textoOriginal ) throws Exception
	{
		ArrayList<String> asd = proyecto.getListaStringElementos();
		
		String JsonArray = JsonUtil.devolverJsonArray(proyecto.getListaStringElementos());
		
		
		String jsonBody = "{\"id_ude\":\"" + proyecto.getCodigoUde()
						+ "\",\"titulo\":\"" + proyecto.getTitulo()
						+ "\",\"anio\":\"" + proyecto.getAnio()
						+ "\",\"tutor\":\"" + proyecto.getTutorString()
						+ "\",\"contenido\":\"" + FuncionesTexto.limpiarTexto(textoOriginal)
						+ "\",\"elemento\":" + JsonArray
						+ "}";

		StringBuilder builder = new StringBuilder();
		
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		builder.append("proyectos/");
		builder.append(Integer.toString(proyecto.getId()));
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");		
		
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
	public ArrayList<ResultadoBusqueda> realizarBusquedaES(String busquedaOriginal) throws Exception
	{
		String busqueda = busquedaOriginal;
		String jsonBody;
		boolean esBusquedaDirecta = true;
		
		ArrayList<Elemento> elementos = this.obtenerElementoString(busqueda);	
		
		if (elementos != null && !elementos.isEmpty())
		{
			esBusquedaDirecta = false;
			busqueda = this.obtenerStringDesdeListaElemento(elementos);
			// hay que tunearla
			jsonBody = "{\"query\":{\"match_phrase\":{\"elemento\":\"" + busqueda + "\"}},\"highlight\":{\"fields\":{\"elemento\":{}}}}";
		}
		else
		{
			jsonBody = "{\"query\":{\"match\":{\"contenido\":\"" + busqueda + "\"}},\"highlight\":{\"fields\":{\"contenido\":{}}}}";	
		}		
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		builder.append("_search");
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		String response = HttpUtil.doPostWithJsonBody(builder.toString(), headers, jsonBody, Constantes.ElasticSearch_Timeout);
		
		return obtenerResultadoDesdeJson(response, esBusquedaDirecta);
		
	}

	private String obtenerStringDesdeListaElemento(ArrayList<Elemento> elementos) 
	{
		String retorno="";
		for(Elemento elem : elementos)
		{
			retorno= retorno + " " + elem.getNombre();
		}
		return retorno.trim();
	}
	
	private ArrayList<ResultadoBusqueda> obtenerResultadoDesdeJson(String json, boolean esBusquedaDirecta) throws Exception
	{
		ArrayList<ResultadoBusqueda> resultado= new ArrayList<ResultadoBusqueda>();
		JsonObject jsonObject = JsonUtil.parse(json);
		
		Iterator<JsonValue> iterador = jsonObject.getJsonObject("hits").getJsonArray("hits").iterator();
		while (iterador.hasNext())
		{	
			JsonValue jsonValue = iterador.next();

			ResultadoBusqueda resultadoBusqueda= new ResultadoBusqueda();
			
			String id = jsonValue.asJsonObject().getString("_id");
			String score = jsonValue.asJsonObject().getJsonNumber("_score").toString();
			String titulo = jsonValue.asJsonObject().getJsonObject("_source").getString("titulo");
			String codigoUde = jsonValue.asJsonObject().getJsonObject("_source").getString("id_ude");
			String anio= jsonValue.asJsonObject().getJsonObject("_source").getString("anio");
			
			Iterator<JsonValue> iterHighlight;
			if (esBusquedaDirecta)
			{
				iterHighlight = jsonValue.asJsonObject().getJsonObject("highlight").getJsonArray("contenido").iterator();
			}
			else
			{
				iterHighlight = jsonValue.asJsonObject().getJsonObject("highlight").getJsonArray("elemento").iterator();
			}
			
			ArrayList<String> resultadosHighlight = new ArrayList<>();
			while (iterHighlight.hasNext())
			{
				JsonValue jsonValueHighlight = iterHighlight.next();
				String highlight = jsonValueHighlight.toString();
				resultadosHighlight.add(highlight);
			}
						
			resultadoBusqueda.setIdProyecto(Integer.parseInt(id));
			resultadoBusqueda.setScore(Float.parseFloat(score));
			resultadoBusqueda.setTituloProyecto(titulo);
			resultadoBusqueda.setCodigoUde(codigoUde);
			resultadoBusqueda.setAnio(Integer.parseInt(anio));
			resultadoBusqueda.setHighlight(resultadosHighlight);
			
			resultado.add(resultadoBusqueda);
		}
		return resultado;
	}
}
