package de.fhws.hablame.chatbotbackend.extension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * This class is an extension to answer questions for people and entities by using Wikipedia API
 * @author Christian
 */
public class ExtensionWikipedia implements AIMLProcessorExtension{

	private final String wikipediaExtractUrl = "https://de.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=";
	private final String wikipediaSearchUrl = "https://de.wikipedia.org/w/api.php?format=json&action=query&list=search&srsearch=";
	private final String noResult = "Leider habe ich keine Antwort finden können.";
	private HttpHandler httpHandler = new HttpHandler();
	private static final Logger LOG = LoggerFactory.getLogger(ExtensionWikipedia.class);
	private Set<String> extensionTagNames = Utilities.stringSet(ExtensionStringHolder.ENTITYTAG);
	
	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		String answer = "";
		String input = AIMLProcessor.evalTagContent(node, parseState, null);
		//User Question: WER IST * / WAS IST EIN * / WAS IST EINE *
		if (node.getNodeName().equals(ExtensionStringHolder.ENTITYTAG)) {
			try {
				answer = getFirstSentence(shortenExtract(handleRequest(input)));
			} 
			catch (JSONException e) {
				LOG.warn("Problem occured while requesting the Wikipedia extract of a Person");
			}
		}
		return answer;
	}
	
	private String handleRequest(String userInput) throws JSONException {
		String extract = "";
		String searchResult = "";
		JSONObject jsonResponse = performRequest(userInput);
		if(checkResponseForExtract(jsonResponse) && checkIfResponseIsNoDefinition(getExtractFromJson(jsonResponse))) {
			extract = getExtractFromJson(jsonResponse);
		}
		else {
			searchResult = performSearch(userInput);
			if(searchResult == noResult)
				extract = searchResult;
			else {
				jsonResponse = performRequest(insertUnderscore(searchResult));
				if(checkResponseForExtract(jsonResponse) && checkIfResponseIsNoDefinition(getExtractFromJson(jsonResponse))) {
					extract = getExtractFromJson(jsonResponse);
				}
				else {
					extract = noResult;
				}
			}
		}
		return extract;
	}
	
	private JSONObject performRequest(String userInput) throws JSONException {
		JSONObject query = new JSONObject(httpHandler.callApi(wikipediaExtractUrl, insertUnderscore(userInput)));
		return query;
	}
	
	private String performSearch(String searchInput) throws JSONException {
		String searchOutput = null;
		JSONObject query = new JSONObject(httpHandler.callApi(wikipediaSearchUrl, insertUnderscore(searchInput)));
		if(checkIfJsonContainsResult(query)) {
			searchOutput = query.getJSONObject("query").getJSONArray("search").getJSONObject(0).getString("title");
		}
		else {
			searchOutput = noResult;
		}
		return searchOutput;
	}
	
	private JSONObject getWikipediaPageObject(JSONObject input) throws JSONException{
		String key = null;
		JSONObject pages = input.getJSONObject("query").getJSONObject("pages");
		Iterator<?> iterator = pages.keys();	
		while(iterator.hasNext()) {
			key = (String) iterator.next();
		}
		return pages.getJSONObject(key);
	}
	
	private boolean checkResponseForExtract(JSONObject query) throws JSONException {
		if(getWikipediaPageObject(query).has("extract") && (getExtractFromJson(query).isEmpty() == false)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean checkIfJsonContainsResult(JSONObject query) throws JSONException {
		if(query.getJSONObject("query").getJSONObject("searchinfo").getInt("totalhits") == 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean checkIfResponseIsNoDefinition(String extract) {
		if((extract.contains("bezeichnet:"))||(extract.contains("steht für:"))||(extract.contains("folgender Personen:"))) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private String insertUnderscore(String input) {
		String output = input.replace(" ", "_");
		return output;
	}
	
	private String shortenExtract(String input) {
		String output = input.replaceAll("\\[.*?\\] ?", "").replaceAll("\\(.*?\\) ?", "");
		Pattern whitespace = Pattern.compile("\\s\\.");
		Matcher matcher = whitespace.matcher(output);
		output = matcher.replaceAll(".");
		return output;
	}
	
	private String getExtractFromJson(JSONObject query) throws JSONException {
		return getWikipediaPageObject(query).getString("extract");
	}
	
	private String getFirstSentence(String input) {
		int index = 0;
		String output;
		List<String> inputSplit = Arrays.asList(input.split("(?<=[a-zA-Z])(\\.)"));
		for(ListIterator<String> jndex = inputSplit.listIterator(); jndex.hasNext();) {
			String element = jndex.next();
			jndex.set(element + ".");
		}
		
		output = inputSplit.get(index);
		while((inputSplit.get(index).endsWith("z.B."))||(inputSplit.get(index).endsWith("usw."))||
				(inputSplit.get(index).endsWith("bzw."))) {
			index++;
			output += inputSplit.get(index);
		}
		return output;
	}
}