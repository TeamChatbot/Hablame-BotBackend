package de.fhws.hablame.chatbotbackend.extension;

import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

/**
 * This extension class is a prototype for the chatbot to call external APIs (in this case the openweather API)
 * and display the result for the user
 * @author David
 *
 */
public class ExtensionWeather implements AIMLProcessorExtension {
	
	private HttpHandler httpHandler = new HttpHandler();
	private static final Logger LOG = LoggerFactory.getLogger(ExtensionWeather.class);
	private Set<String> extensionTagNames = Utilities.stringSet(ExtensionStringHolder.WEATHERTAG, ExtensionStringHolder.TEMPERATURTAG);

	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		String answer = "";
		String city = AIMLProcessor.evalTagContent(node, parseState, null);
		if (node.getNodeName().equals(ExtensionStringHolder.WEATHERTAG)) {
			answer = getActualWeather(city);
		} 
		else if(node.getNodeName().equals(ExtensionStringHolder.TEMPERATURTAG)) {
			answer = getActualTemperatur(city);
		}
		return answer;
	}

	/**
	 * Helper method get the actual temperature
	 * @return
	 */
	private String getActualTemperatur(String city) {
		String temperature = "";
		String openWeatherMapUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&lang=de&appid=8de2700b042d4e09a990b08da30a6afe";
		try {
			JSONObject jObj = new JSONObject(httpHandler.callApi(openWeatherMapUrl, "").replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss").replace(" ", ""));
			temperature =  Long.toString(Math.round(jObj.getJSONObject("main").getDouble("temp")));
		} catch (JSONException e) {
			LOG.warn("Problem occured while parsing the json response for actual temperature");
		}
		return temperature;
	}

	/**
	 * Helper method to get the actual weather
	 * @return
	 */
	private String getActualWeather(String city) {
		String weather = "";
		String openWeatherMapUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&lang=de&appid=8de2700b042d4e09a990b08da30a6afe";
		try {
			JSONObject jObj = new JSONObject(httpHandler.callApi(openWeatherMapUrl, "").replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss").replace(" ", ""));
			weather =  jObj.getJSONArray("weather").getJSONObject(0).getString("description");
		} catch (JSONException e) {
			LOG.warn("Problem occured while parsing the json response for actual weather");
		}
		return weather;
	}
}