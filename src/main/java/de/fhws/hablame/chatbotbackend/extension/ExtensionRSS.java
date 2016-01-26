package de.fhws.hablame.chatbotbackend.extension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

/**
 * This class is an extension to fetch soccer results from kicker.de using RSS
 * @author Christian
 */
public class ExtensionRSS implements AIMLProcessorExtension {
	
	private final String kickerURL = "http://rss.kicker.de/live/bundesliga";
	private URL feedSource = null;
	private SyndFeed feed = null;
	private String result = "";
	private static final Logger LOG = LoggerFactory.getLogger(ExtensionRSS.class);
	private Set<String> extensionTagNames = Utilities.stringSet(ExtensionStringHolder.FOOTBALLTAG);

	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		String answer = "";
		String input = AIMLProcessor.evalTagContent(node, parseState, null);
		//User Question: WIE HABEN DIE * GESPIELT / WIE HAT DER * GESPIELT / WIE HAT * GESPIELT
		if (node.getNodeName().equals(ExtensionStringHolder.FOOTBALLTAG)) {
			answer = handleGetResult(input);
		}
		return answer;
	}
	
	/**
	 * Central method for handling the RSS Feed request
	 */
	private String handleGetResult(String inputTeam) {
		feed = getRSSFeed();
        if(feed != null) {
        	result = editResult(searchEntriesForResult(feed, inputTeam));
        } 
		return result;
	}
	
	/**
	 * Helper method to search the input team (from user) in the feed and if applicable returns result of the game
	 */
	private String searchEntriesForResult(SyndFeed feed, String inputTeam) {
		String result = "";
		List<SyndEntry> entrieList = feed.getEntries();
    	Iterator<SyndEntry> entriesIterator = entrieList.iterator();
    	while(entriesIterator.hasNext()) {
    		SyndEntry syndEntry = entriesIterator.next();
    		if((syndEntry.getTitle().toLowerCase()).contains(inputTeam.toLowerCase())) {
    			result = syndEntry.getTitle();
    		}
			List<SyndCategory> categories = syndEntry.getCategories();
			for(int indexCategories = 0; indexCategories < 4; indexCategories++) {
				String name = categories.get(indexCategories).getName().toLowerCase();
				if(name.contains(inputTeam.toLowerCase())) {
					result = syndEntry.getTitle();
				}
			}
    	}
        if(result == "") {
        	result = "Das weis ich leider nicht.";
        }
        else if(checkIfGameIsInFuture(result)){
        	result = result + " hat noch nicht stattgefunden";
        }
    	return result;
	}
	
	/**
	 * Helper method to make the result looking like an authentic soccer result
	 */
	private String editResult(String result) {
        String newResult = result.replaceAll("\\(.*?\\) ?", "").replace("-", "gegen").replace(":", " zu ").replace("1.", "ersten");	
		return newResult;
	}

	/**
	 * Helper method to fetch the RSS Feed from kicker.de
	 */
	private SyndFeed getRSSFeed() {
		try {
			feedSource = new URL(kickerURL);
		} 
		catch (MalformedURLException e) {
			LOG.warn("URL was malformed");
		}
        SyndFeedInput input = new SyndFeedInput();
        try {
			feed = input.build(new XmlReader(feedSource));
		} catch (IllegalArgumentException | FeedException | IOException e) {
			LOG.warn("Failed to read rss feed from web-source");
		}
		return feed;
	}
	
	private Boolean checkIfGameIsInFuture(String input) {
		if(input.contains("Uhr")) {
			return true;
		}
		else {
			return false;
		}
	}
}