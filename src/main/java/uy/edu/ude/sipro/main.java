package uy.edu.ude.sipro;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;


public class main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	String requestUrl = "http://localhost:9200" + "/" + "sipro_index" + "/" + "_search";

	HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
	HttpClient httpClient = httpClientBuilder.build();

	try
	{
	    HttpPost request = new HttpPost(requestUrl);

	    RequestConfig requestConfig = RequestConfig.custom()
	            .setSocketTimeout(80)
	            .setConnectTimeout(80)
	            .setConnectionRequestTimeout(80)
	            .build();

	    request.setConfig(requestConfig);

	    StringEntity params = new StringEntity("{\"query\":{\"match\":{\"bio\":\"Universidad\"}},\"highlight\":{\"fields\":{\"bio\":{}}}}");
	    request.addHeader("content-type", "application/json");
	    request.addHeader("Accept","application/json");
	    request.setEntity(params);
	    HttpResponse response = httpClient.execute(request);

	    String json = EntityUtils.toString(response.getEntity());
	    System.out.println(json);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}


	}
}

