package uy.edu.ude.sipro.utiles;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.json.JSONArray;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Sinonimo;


public abstract class JsonUtil 
{
	public static JsonObject parse(String jsonString) throws Exception
	{
		JsonReader jsonReader = null;
		try
		{
			jsonReader = Json.createReader(new ByteArrayInputStream(jsonString.getBytes("UTF-8")));
			return jsonReader.readObject();
		}
		catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(jsonReader != null)
			{
				jsonReader.close();
			}
		}
	}
	
	public static String devolverJsonArray(ArrayList<String> listaStrings)
	{
		String retorno = "[]";
		if (listaStrings != null && !listaStrings.isEmpty())
		{
			retorno = "[";
			for (String str : listaStrings)
			{
				if (str != null)
				{
					retorno = retorno + "\"" + str + "\",";
				}
			}
			if (retorno.charAt(retorno.length()-1)==',')
			{
				retorno = retorno.substring(0, retorno.length() - 1);
			}
			retorno = retorno + "]";		
		}		
		return retorno;
	}
	
	public static String devolverJsonArrayElementosSinonimos(ArrayList<Elemento> elementos)
	{
		ArrayList<String> listaStrings = new ArrayList<String>();		
		if (elementos != null && !elementos.isEmpty())
		{
			for(Elemento elem : elementos)
			{
				if (elem.getSinonimos() != null && !elem.getSinonimos().isEmpty())
				{
					listaStrings.add(devolverStringElementoSinonimos(elem));
				}
			}
		}
		return devolverJsonArray(listaStrings);
	}

	private static String devolverStringElementoSinonimos(Elemento elem)
	{
		String retorno = "";
		for(Sinonimo sin : elem.getSinonimos())
		{
			retorno = retorno + "," + sin.getNombre();
		}
		retorno = retorno.substring(1) + " => " + elem.getNombre();
		return retorno;
	}
}
