package uy.edu.ude.sipro.service.implementacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.service.interfaces.BusquedaService;
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
	
	@Override
	public String buscarElementosProyectoES(ArrayList<Elemento> elementos) throws Exception
	{
		
		String busqueda= this.obtenerStringDesdeListaElemento(elementos);
		String jsonBody = "{\"query\":{\"match\":{\"Elemento\":\"" + busqueda + "\"}},\"highlight\":{\"fields\":{\"Contenido\":{}}}}";
		StringBuilder builder = new StringBuilder();
		
		builder.append(Constantes.ElasticSearch_Url_Base);
		builder.append(Constantes.ElasticSearch_Index);
		builder.append("_search");
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		
		String response = HttpUtil.doPostWithJsonBody(builder.toString(), headers, jsonBody, Constantes.ElasticSearch_Timeout);
		
		JsonObject jsonObject = JsonUtil.parse(response);
		
		return jsonObject.getJsonObject("hits").getJsonArray("hits").getJsonObject(0).getJsonObject("highlight").toString();
		
		
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

}
