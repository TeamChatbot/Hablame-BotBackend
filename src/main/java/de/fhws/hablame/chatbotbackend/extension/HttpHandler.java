package de.fhws.hablame.chatbotbackend.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class offers a method to handle http requests
 * @author Christian
 */
public class HttpHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpHandler.class);

	public String callApi(String inputUrl, String userInput) {
		StringBuffer stringBuffer = new StringBuffer();
		URL url = null;
		
		try {
			System.out.println("Link of API Request: " +inputUrl+userInput);
			url = new URL(inputUrl+userInput);
		}
		catch (MalformedURLException e) {
			LOG.warn("URL was malformed");
		}
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(RequestMethod.GET.toString());
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			stringBuffer = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				stringBuffer.append(inputLine);
			}
			in.close();
		}
		catch (IOException e) {
			LOG.warn("Problem occured while doing the API request");
		}
		return stringBuffer.toString();
	}
}