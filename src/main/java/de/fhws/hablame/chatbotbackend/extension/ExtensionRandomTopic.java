package de.fhws.hablame.chatbotbackend.extension;

import java.util.Random;
import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Topic;
import de.fhws.hablame.chatbotbackend.service.CategoryService;
import de.fhws.hablame.chatbotbackend.service.TopicService;
import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

public class ExtensionRandomTopic implements AIMLProcessorExtension {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TopicService topicService;
	
	private Set<String> extensionTagNames = Utilities.stringSet(ExtensionStringHolder.RANDOM_TOPIC);

	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}

	@Override
	public String recursEval(Node node, ParseState parseState) {
		if (node.getNodeName().equals(ExtensionStringHolder.RANDOM_TOPIC)) {
			return selectRandomTopic(node, parseState);
		} else {
			return AIMLProcessor.genericXML(node, parseState);
		}
	}

	private String selectRandomTopic(Node node, ParseState parseState) {
		long numberOfCategories = categoryService.count();
		if (numberOfCategories > 0) {
			//we assume, numberOfCategories would not reach 2147483647 (int max value), so the cast is acceptable
			Category category = categoryService.getById(Long.valueOf(new Random().nextInt((int)numberOfCategories)));
			if (category != null) {
				Topic topic = topicService.getTopicById(Long.valueOf(new Random().nextInt(category.getTopic().size())));
				if (topic != null) {
					parseState.topic = String.format(ExtensionStringHolder.SMALLTALK_TOPIC_PATTERN, 
							category.getName(), topic.getName());
				    parseState.chatSession.predicates.put(ExtensionStringHolder.TOPIC, parseState.topic );
				}
			}
		}
		return "";
	}

}
