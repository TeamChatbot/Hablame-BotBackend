package de.fhws.hablame.chatbotbackend.extension;

import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

/**
 * This class is an extension to fetch factual knowledge from Wikidata API
 * @author Christian
 */
public class ExtensionWikidata implements AIMLProcessorExtension {
	
	private final String wikidataGetEntityIdUrl = "https://www.wikidata.org/w/api.php?action=wbsearchentities&format=json&language=de&type=item&limit=1&search=";
	private final String wikidataGetEntityNameUrl = "https://www.wikidata.org/w/api.php?action=wbgetentities&props=labels&format=json&languages=de&ids=Q";
	private final String wikidataGetEntityUrl = "http://www.wikidata.org/wiki/Special:EntityData/";
	private final String wikidataQueryApiUrl = "http://wdq.wmflabs.org/api?q=";
	private final String noResult = "Das weis ich leider noch nicht.";
	private String entityId = "";
	private int capitalCityId = 0;
	private HttpHandlerApache httpHandler = new HttpHandlerApache();
	private static final Logger LOG = LoggerFactory.getLogger(ExtensionWikidata.class);
	private Set<String> extensionTagNames = Utilities.stringSet(ExtensionStringHolder.CAPITALCITYTAG, ExtensionStringHolder.POPULATIONTAG1);

	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		String answer = "";
		String input = AIMLProcessor.evalTagContent(node, parseState, null);
		try {
			//User Question: WAS IST DIE HAUPTSTADT VON *
			if (node.getNodeName().equals(ExtensionStringHolder.CAPITALCITYTAG)) {
				answer = handleCapitalCityRequest(input);
			}
			//User Question: WIE VIELE EINWOHNER HAT *
			else if (node.getNodeName().equals(ExtensionStringHolder.POPULATIONTAG1)) {
				answer = Integer.toString(handlePopulationRequest(input));			
			}
		} 
		catch (JSONException e) {
			LOG.warn("Problem occured while requesting the capitalcity from wikidata");
		}
		if(answer == "") {
			answer = noResult;
		}
		return answer;
	}
	
	private String handleCapitalCityRequest(String userInput) throws JSONException {
		searchForEntityId(userInput);
		String capitalId = getCapitalCityId(entityId);
		String capitalName = getCapitalCityName(capitalId);
		return capitalName;
	}
	
	private int handlePopulationRequest(String userInput) throws JSONException {
		searchForEntityId(userInput);
		int population = getPopulation(getFullEntity(entityId));
		return population;
	}
	
	private void searchForEntityId (String entityName) throws JSONException {
		JSONObject search = new JSONObject(httpHandler.callApi(wikidataGetEntityIdUrl, entityName));
		entityId = search.getJSONArray("search").getJSONObject(0).getString("id");
	}
	
	private JSONObject getFullEntity(String entityId) throws JSONException {
		JSONObject entity = new JSONObject(httpHandler.callApi(wikidataGetEntityUrl, entityId));
		return entity;
	}
	
	private int getPopulation(JSONObject entity) throws JSONException {
		JSONObject entities = entity.getJSONObject("entities");
		JSONArray population = entities.getJSONObject(entityId).getJSONObject("claims").getJSONArray("P1082");
		int populationNumber = population.getJSONObject(0).getJSONObject("mainsnak").getJSONObject("datavalue").getJSONObject("value").getInt("amount");
		return populationNumber;
	}
	
	private String getCapitalCityId(String cityId) throws JSONException {
		int newCityId = Integer.parseInt(cityId.substring(1));
		int index = -1;
		String cityIdUrl = "tree[" + newCityId + "][36]";
		JSONObject city = new JSONObject(httpHandler.callApi(wikidataQueryApiUrl, cityIdUrl));
		do {
			index++;
			capitalCityId = city.getJSONArray("items").getInt(index);
		}
		while(capitalCityId == newCityId);
		return Integer.toString(capitalCityId);
	}
	
	private String getCapitalCityName(String capitalCityId) throws JSONException {
		JSONObject cityName = new JSONObject(httpHandler.callApi(wikidataGetEntityNameUrl, capitalCityId));
		String capitalCityName = cityName.getJSONObject("entities").getJSONObject("Q"+capitalCityId).getJSONObject("labels").getJSONObject("de").getString("value");
		return capitalCityName;
	}
}