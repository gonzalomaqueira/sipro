package uy.edu.ude.sipro.utiles;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtil 
{
	public static String doPost(String url, int timeout) throws Exception
	{
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient httpClient = httpClientBuilder.build();

		try
		{
		    HttpPost request = new HttpPost(url);

		    RequestConfig requestConfig = RequestConfig.custom()
		            .setSocketTimeout(timeout)
		            .setConnectTimeout(timeout)
		            .setConnectionRequestTimeout(timeout)
		            .build();

		    request.setConfig(requestConfig);

		    HttpResponse response = httpClient.execute(request);

		    return EntityUtils.toString(response.getEntity());
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public static String doPostWithJsonBody(String url, HashMap<String, String> headers, String jsonBody, int timeout) throws Exception
	{
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient httpClient = httpClientBuilder.build();

		try
		{
		    HttpPost request = new HttpPost(url);

		    RequestConfig requestConfig = RequestConfig.custom()
		            .setSocketTimeout(timeout)
		            .setConnectTimeout(timeout)
		            .setConnectionRequestTimeout(timeout)
		            .build();

		    request.setConfig(requestConfig);

		    StringEntity params = new StringEntity(jsonBody, StandardCharsets.UTF_8);
		    headers.forEach((v,k) -> request.addHeader(v,k));
		    
		    request.setEntity(params);
		    HttpResponse response = httpClient.execute(request);

		    return EntityUtils.toString(response.getEntity());
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public static String doPutWithJsonBody(String url, HashMap<String, String> headers, String jsonBody, int timeout) throws Exception
	{
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient httpClient = httpClientBuilder.build();

		try
		{
		    HttpPut request = new HttpPut(url);

		    RequestConfig requestConfig = RequestConfig.custom()
		            .setSocketTimeout(timeout)
		            .setConnectTimeout(timeout)
		            .setConnectionRequestTimeout(timeout)
		            .build();

		    request.setConfig(requestConfig);

		    StringEntity params = new StringEntity(jsonBody, StandardCharsets.UTF_8);
		    headers.forEach((v,k) -> request.addHeader(v,k));
		    
		    request.setEntity(params);
		    HttpResponse response = httpClient.execute(request);

		    return EntityUtils.toString(response.getEntity());
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	public static String doDelete(String url, int timeout) throws Exception
	{
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient httpClient = httpClientBuilder.build();

		try
		{
			HttpDelete request = new HttpDelete(url);

		    RequestConfig requestConfig = RequestConfig.custom()
		            .setSocketTimeout(timeout)
		            .setConnectTimeout(timeout)
		            .setConnectionRequestTimeout(timeout)
		            .build();

		    request.setConfig(requestConfig);
		    HttpResponse response = httpClient.execute(request);

		    return EntityUtils.toString(response.getEntity());
		}
		catch(Exception e)
		{
			throw e;
		}
	}
}
