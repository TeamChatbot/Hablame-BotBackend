package de.fhws.hablame.chatbotbackend.extension;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Topic;
import de.fhws.hablame.chatbotbackend.service.CategoryService;
import de.fhws.hablame.chatbotbackend.service.TopicService;
import de.fhws.hablame.chatbotbackend.service.chatbot.ChatbotService;
import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

public class ExtensionRandomValueOfTopic implements AIMLProcessorExtension {
	
	@Autowired
	private ChatbotService chatbotInitiating;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TopicService topicService;
	
	private static final Logger LOG = LoggerFactory.getLogger(ExtensionRandomValueOfTopic.class);
	
	private Set<String> extensionTagNames = Utilities.stringSet(ExtensionStringHolder.RANDOM_VALUE);

	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		if (node.getNodeName().equals(ExtensionStringHolder.RANDOM_VALUE)) {
			return selectValue(node, parseState);
		} else {
			return AIMLProcessor.genericXML(node, parseState);
		}
	}

	private String selectValue(Node node, ParseState parseState) {
		String lastSelected = ExtensionBrainSetAndGet.LAST_SELECTED_VALUE;
		if (lastSelected == null) {
			String[] topicSplit = parseState.topic.split("\\.");
			String category = topicSplit[1];
			String topic = topicSplit[2];
			if (category == null || topic == null) {
				LOG.warn("Es ist keine Kategorie und kein Topic vorhanden");
			} else if (category != null && topic != null) {
				//TODO: (look at original class)
				//problem is, that they use a list of objects and also create a new list in the get(), when no category
				//is found. We could solve this by writing a convenient method which also creates a new category
				//if no category with the given name is found.
				//
				//by now we do the random choosing of a topic by finding all and take a random of those topics.
				Category foundCategory = categoryService.getCategoryByName(category);
				List<Topic> topics = foundCategory.getTopic();
				return topics.get(new Random().nextInt(topics.size())).getName();
			}
		} else {
			ExtensionBrainSetAndGet.LAST_SELECTED_VALUE = null;
			return lastSelected;
		}
		return null;
	}

}
