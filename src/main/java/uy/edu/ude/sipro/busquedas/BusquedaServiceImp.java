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
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.utiles.Constantes;
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
	
	@Override
	public ArrayList<ResultadoBusqueda> buscarElementosProyectoES(ArrayList<Elemento> elementos) throws Exception
	{
		
		String busqueda= this.obtenerStringDesdeListaElemento(elementos);
		String jsonBody = "{\"query\":{\"match\":{\"elemento\":\"" + busqueda + "\"}},\"highlight\":{\"fields\":{\"elemento\":{}}}}";
		StringBuilder builder = new StringBuilder();
		
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		builder.append("_search");
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		String response = HttpUtil.doPostWithJsonBody(builder.toString(), headers, jsonBody, Constantes.ElasticSearch_Timeout);
		
		return obtenerResultadoDesdeJson(response);
		
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
	
	private ArrayList<ResultadoBusqueda> obtenerResultadoDesdeJson(String json) throws Exception
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
			
			Iterator<JsonValue> iterHighlight = jsonValue.asJsonObject().getJsonObject("highlight").getJsonArray("elemento").iterator();		
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
			resultadoBusqueda.setHighlight(resultadosHighlight);
			
			resultado.add(resultadoBusqueda);
		}
		return resultado;
	}
}
