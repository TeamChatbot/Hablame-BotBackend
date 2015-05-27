package de.fhws.hablame.chatbotbackend.extension;

import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.service.chatbot.ChatbotService;
import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

public class ExtensionBrainSetAndGet implements AIMLProcessorExtension {
	
	@Autowired
	private ChatbotService chatbotService;
	
	public static String LAST_SELECTED_VALUE = null;

	/*
	 * (non-Javadoc)
	 * @see org.alicebot.ab.AIMLProcessorExtension#extensionTagSet()
	 * The original implementation claims, that the common way of creating a Set<String> with the tagNames
	 * of the extension "results in a wrong set that doesn't match tags correctly anymore".
	 */
	@Override
	public Set<String> extensionTagSet() {
		Set<String> stringSetBrainGet = Utilities.stringSet(ExtensionStringHolder.BRAIN_GET);
		stringSetBrainGet.addAll(Utilities.stringSet(ExtensionStringHolder.BRAIN_SET));
		return stringSetBrainGet;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		if (node.getNodeName().equals(ExtensionStringHolder.BRAIN_GET)) {
			return tagGet(node, parseState);
		} else if (node.getNodeName().equals(ExtensionStringHolder.BRAIN_SET)) {
			return tagSet(node, parseState);
		} else {
			return AIMLProcessor.genericXML(node, parseState);
		}
	}

	/**
	 * Method to write a Tag into the db
	 * @param node
	 * @param parseState
	 * @return
	 */
	private String tagSet(Node node, ParseState parseState) {
		String value = AIMLProcessor.evalTagContent( node, parseState, null );
	    String topic = chatbotService.getAttributeOrTagValue( node, parseState, ExtensionStringHolder.TOPIC );
	    String category = chatbotService.getAttributeOrTagValue( node, parseState, ExtensionStringHolder.CATEGORY );
	    Boolean multiple = ( Boolean.valueOf( chatbotService.getAttributeOrTagValue( node, parseState, ExtensionStringHolder.MULTIPLE ) ) );
	    chatbotService.createCategoryAndTopicAndContentByNames(category, topic, value, multiple);
	    parseState.topic = String.format(ExtensionStringHolder.SMALLTALK_TOPIC_PATTERN, category, topic);
	    parseState.chatSession.predicates.put(ExtensionStringHolder.TOPIC, parseState.topic);
	    //TODO: check if this is necessary, because we wrote to db already
//	    ModelChatbotBrain.getInstance().getDatasource().writeAll( ModelChatbotBrain.getInstance() );
	    LAST_SELECTED_VALUE = value.trim();
	    return "";
	}

	/**
	 * Method to retreive a tag (category name) from the db
	 * @param node
	 * @param parseState
	 * @return
	 */
	private String tagGet(Node node, ParseState parseState) {
		String categoryFromDOM = chatbotService.getAttributeOrTagValue(node, parseState, ExtensionStringHolder.CATEGORY);
		String topicFromDOM = chatbotService.getAttributeOrTagValue(node, parseState, ExtensionStringHolder.TOPIC);
		Category category = chatbotService.getCategoryByCategoryAndTopic(categoryFromDOM, topicFromDOM);
		return category.getName();
	}

}
