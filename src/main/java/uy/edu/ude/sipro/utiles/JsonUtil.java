package uy.edu.ude.sipro.utiles;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.json.JSONArray;


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
}