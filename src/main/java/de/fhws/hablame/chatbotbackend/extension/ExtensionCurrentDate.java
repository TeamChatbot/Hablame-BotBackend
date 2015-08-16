package de.fhws.hablame.chatbotbackend.extension;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.w3c.dom.Node;

//TODO: this class is unnecessary, because the chatbot is able to this stuff by its own with "<date/>"
public class ExtensionCurrentDate implements AIMLProcessorExtension {
	
	private static final String CURRENTDATE = "currentDate";
	private static final String EXPRESSION = "expression";
	
	private Set<String> extensionTagNames = Utilities.stringSet(CURRENTDATE, EXPRESSION);

	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		if (node.getNodeName().equals(CURRENTDATE)) {
			return new SimpleDateFormat("dd.mm.yyyy hh:mm:ss").format(new Date());
		} else if (node.getNodeName().equals(EXPRESSION)) {
			return null;
		} else {
			return AIMLProcessor.genericXML(node, parseState);
		}
	}

}
