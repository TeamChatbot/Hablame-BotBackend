package de.fhws.hablame.chatbotbackend.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class offers a method to handle http requests by using Apache HttpClient
 * @author Christian
 */
public class HttpHandlerApache {
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpHandlerApache.class);
	
	public String callApi(String inputUrl, String userInput) {

		StringBuffer stringBuffer = new StringBuffer();
	    final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setSoTimeout(httpParams, 1500);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpGet request = new HttpGet(inputUrl + userInput);
		HttpResponse response = null;
		BufferedReader bufferedReader = null;
		System.out.println("Link of API Request: " +inputUrl+userInput);
		try {
			response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
		} 
		catch (UnsupportedOperationException | IOException e) {
			LOG.warn("Problem occured while doing the HTTP request");
		}
		return stringBuffer.toString();
	}
}