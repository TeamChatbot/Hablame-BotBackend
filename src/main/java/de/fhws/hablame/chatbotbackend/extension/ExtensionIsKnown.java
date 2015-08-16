package de.fhws.hablame.chatbotbackend.extension;

import java.util.Set;

import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.service.chatbot.ChatbotService;
import de.fhws.hablame.chatbotbackend.utility.ApplicationContextProvider;
import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

/**
 * This Extension can only be used within the <name/> tag of a <condition/> tag
 * It checks whether the requested information exists in database.
 * Then it stores the boolean value in the bot predicates, which then
 * can be used in as values in <li/> tags.
 */
public class ExtensionIsKnown implements AIMLProcessorExtension {
	
	@Autowired
	private ChatbotService chatbotService;
	
	private Set<String> tagSet = Utilities.stringSet(ExtensionStringHolder.ISKNOWN);

	@Override
	public Set<String> extensionTagSet() {
		return tagSet;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		if (node.getNodeName().equals(ExtensionStringHolder.ISKNOWN)) {
			if (chatbotService == null) {
				//in the context of the bot (e.g. the extensions), 
				// we need to manually get the bean to help spring find its context
				chatbotService = ApplicationContextProvider.getApplicationContext().getBean("chatbotService", ChatbotService.class);
			}
			String categoryFromDOM = chatbotService.getAttributeOrTagValue(node, parseState, ExtensionStringHolder.CATEGORY);
			String topicFromDOM = chatbotService.getAttributeOrTagValue(node, parseState, ExtensionStringHolder.TOPIC);
			Category category = chatbotService.getCategoryByCategoryAndTopic(categoryFromDOM, topicFromDOM);
			if (category != null) {
				parseState.chatSession.predicates.put(ExtensionStringHolder.PREDICATE, category.getName());
			} else {
				parseState.chatSession.predicates.put(ExtensionStringHolder.PREDICATE, ExtensionStringHolder.UNDEFINED);
			}
		}
		return ExtensionStringHolder.PREDICATE;
	}

}
