package de.fhws.hablame.chatbotbackend.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Node;

import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

/**
 * This extension class is a prototype for the chatbot to call external APIs (in this case the openweather API)
 * and display the result for the user
 * @author David
 *
 */
public class ExtensionWeather implements AIMLProcessorExtension {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExtensionWeather.class);
	
	private Set<String> extensionTagNames = Utilities.stringSet(ExtensionStringHolder.WEATHERTAG, ExtensionStringHolder.TEMPERATURTAG);

	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		String answer = "";
		if (node.getNodeName().equals(ExtensionStringHolder.WEATHERTAG)) {
			answer = getActualWeather();
		} else if(node.getNodeName().equals(ExtensionStringHolder.TEMPERATURTAG)) {
			answer = getActualTemperatur();
		}
		return answer;
	}

	/**
	 * Helper method get the actual temperature
	 * @return
	 */
	private String getActualTemperatur() {
		String temperature = "";
		try {
			JSONObject jObj = new JSONObject(callOpenWeatherAPI());
			temperature =  Long.toString(Math.round(jObj.getJSONObject("main").getDouble("temp")));
		} catch (JSONException e) {
			LOG.warn("Problem occured while parsing the json response");
		}
		return temperature;
	}

	/**
	 * Helper method to get the actual weather
	 * @return
	 */
	private String getActualWeather() {
		String weather = "";
		try {
			JSONObject jObj = new JSONObject(callOpenWeatherAPI().replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss"));
			weather =  jObj.getJSONArray("weather").getJSONObject(0).getString("description");
		} catch (JSONException e) {
			LOG.warn("Problem occured while parsing the json response");
		}
		return weather;
	}

	/**
	 * Helper method to call the external open weather API
	 */
	private String callOpenWeatherAPI() {
		StringBuffer stringBuffer = null;
		//String stringUrl = "http://api.openweathermap.org/data/2.5/weather?id=2805615&units=metric&lang=de";
		String stringUrl = "http://api.openweathermap.org/data/2.5/weather?id=2805615&units=metric&lang=de&appid=8de2700b042d4e09a990b08da30a6afe";
		URL url = null;
		try {
			url = new URL(stringUrl);
		} catch (MalformedURLException e1) {
			LOG.warn("URL was malformed");
		}
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(RequestMethod.GET.toString());
			con.setRequestProperty(ExtensionStringHolder.USERAGENTKEY, ExtensionStringHolder.USERAGENTVALUE);
//			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			con.setRequestProperty("Accept-Language", "de,en-US;q=0.7,en;q=0.3");
//			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
//			con.setRequestProperty("Accept-Charset", "UTF-8");
//			con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
//			int resp = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			stringBuffer = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				stringBuffer.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			LOG.warn("Problems occured while doing the weather get request");
		}
		//LOG.info("SRV OUT: "+stringBuffer.toString());
		return stringBuffer.toString();
	}

}
