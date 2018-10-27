package uy.edu.ude.sipro.utiles;

import java.io.ByteArrayInputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;


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
}
