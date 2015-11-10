package de.fhws.hablame.chatbotbackend.service.chatbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.ParseState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.fhws.hablame.chatbotbackend.dto.CategoryDTO;
import de.fhws.hablame.chatbotbackend.dto.ContentDTO;
import de.fhws.hablame.chatbotbackend.dto.TopicDTO;
import de.fhws.hablame.chatbotbackend.extension.AIMLExtensionHub;
import de.fhws.hablame.chatbotbackend.extension.ExtensionBrainSetAndGet;
import de.fhws.hablame.chatbotbackend.extension.ExtensionCurrentDate;
import de.fhws.hablame.chatbotbackend.extension.ExtensionIsKnown;
import de.fhws.hablame.chatbotbackend.extension.ExtensionRandomTopic;
import de.fhws.hablame.chatbotbackend.extension.ExtensionRandomValueOfTopic;
import de.fhws.hablame.chatbotbackend.extension.ExtensionWeather;
import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Content;
import de.fhws.hablame.chatbotbackend.model.Topic;
import de.fhws.hablame.chatbotbackend.service.CategoryService;
import de.fhws.hablame.chatbotbackend.service.ContentService;
import de.fhws.hablame.chatbotbackend.service.TopicService;
import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

@Service
public class ChatbotService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChatbotService.class);
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private ContentService contentService;

	/**
	 * {@link List} to hold instances of each extension for the {@link AIMLExtensionHub}.
	 */
	private static List<AIMLProcessorExtension> default_aiml_extensions = 
			Arrays.asList(
					new ExtensionIsKnown(), 
					new ExtensionCurrentDate(), 
					new ExtensionRandomValueOfTopic(),
					new ExtensionBrainSetAndGet(),
					new ExtensionRandomTopic(),
					new ExtensionWeather());
	
	/**
	 * Class representing the AIML bot
	 */
	private Bot bot;
	
	/**
	 * Name of the {@link Bot}.<br>
	 * We add the value of the chatbot.properties file respective his value "chatbot.name"
	 * to {@link #botName} with the help of {@link Value} 
	 */
	@Value("${chatbot.name}")
	private String botName;
	
	/**
	 * Root path of Program AB.<br>
	 * We add the value of the chatbot.properties file respective his value "chatbot.path"
	 * to {@link #botPath} with the help of {@link Value} 
	 */
	@Value("${chatbot.path}")
	private String botPath;
	
	/**
	 * OpenWeather API-Key for calling their API.<br>
	 * We add the value of the chatbot.properties file respective his value "chatbot.openweatherkey"
	 * to {@link #apiKey} with the help of {@link Value}
	 * TODO: replace the hardcoded string from {@link ExtensionWeather} and use this one here programmatically
	 */
	@Value("${chatbot.openweatherkey}")
	private String apiKey;
	
	/**
	 * Getter for the {@link #apiKey}.
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Class encapsulating a chat session between a bot and a client
	 */
	private Chat chatSession;
	
	/**
	 * Method to handle the conversation with the chatbot.
	 * If necessary, this method calls the {@link #loadExtensions(List)} to instantiate the {@link #bot}
	 * and afterwards to instantiate the {@link #chatSession} for a {@link Chat#multisentenceRespond(String)}.
	 */
	public String conversation(String input) {
		if(bot == null) {
			Bot bot = loadExtensions(default_aiml_extensions);
			chatSession = new Chat(bot);
		}
		//workaround for the English default answer of the bot, when sth. is unknown
		String answer = chatSession.multisentenceRespond(input);
		if (answer.equals("I have no answer for that.")) {
			answer = "Das weis ich leider nicht.";
		}
		return answer;
	}
	
	/**
	 * Method to stop a bot conversation and save aiml and aimlif from the chatbot brain
	 * @return <code>true</code> if bot was instantiated before and <code>false</code> otherwise
	 */
	public boolean stopConversation() {
		if (bot != null) {
			//write learned categories to AIMLIF file
			bot.writeLearnfIFCategories();
			//writeAIMLFiles() and writeAIMLIFFiles() gets executed with this method:
			bot.writeQuit();
			bot = null;
			return true;
		} else {
			LOG.warn("Could not stop the conversation, because the bot was already null");
			return false;
		}
	}
	
	/**
	 * Private method to load the Chatbot's extensions for the initialization.
	 * Additionally it creates an instance of {@link Bot} and one of 
	 * {@link Chat} with the bot as parameter afterwards.
	 * @param extensions
	 */
	private Bot loadExtensions(List<AIMLProcessorExtension> extensions) {
		AIMLProcessor.extension = AIMLExtensionHub.createFromExtensions(extensions);
		/*
		 * because of problems with new chatbot files (maps, sets, aiml-files, etc.) we need 
		 * to set the chatbot path to an absolute.
		 * Before this, we tested it by setting the path to an relative one, as in the following
		 * commented section
		 */
//		URL url = getClass().getClassLoader().getResource(botPath);
//		String absolutePath = url.getPath();
//		bot = new Bot(botName, absolutePath, ExtensionStringHolder.CHAT);
		bot = new Bot(botName, botPath, ExtensionStringHolder.CHAT);
		bot.writeUnfinishedIFCategories();
		return bot;
	}
	
	/**
	 * Method to create a {@link Category}, {@link Topic} and a {@link Content} all at once.
	 * This is used by the {@link ExtensionBrainSetAndGet}.
	 * @param categoryName
	 * @param topicName
	 * @param contentValue
	 * @param multiple
	 * @return the created {@link Category} or null
	 */
	public Category createCategoryAndTopicAndContentByNames(String categoryName, String topicName, 
			String contentValue, boolean multiple) {
		Category category = null;
		if (categoryName != null && topicName != null && contentValue != null) {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setActive(true);
			categoryDTO.setName(categoryName);
			category = categoryService.create(categoryDTO);
			TopicDTO topicDTO = new TopicDTO();
			topicDTO.setActive(true);
			topicDTO.setName(topicName);
			topicDTO.setCategoryId(category.getId());
			Topic topic = topicService.create(topicDTO);
			List<Topic> topics = new ArrayList<>();
			topics.add(topic);
			category.setTopics(topics);
			//TODO: update category?
			ContentDTO contentDTO = new ContentDTO();
			contentDTO.setActive(true);
			contentDTO.setMultiple(multiple);
			contentDTO.setValue(contentValue);
			contentDTO.setTopicId(topic.getId());
			Content content = contentService.create(contentDTO);
			List<Content> contents = new ArrayList<>();
			contents.add(content);
			topic.setContents(contents);
			//TODO: update topic?
			LOG.info("Created category with id {}, topic with id {} and content with id {}", 
					category.getId(), topic.getId(), content.getId());
		} else {
			LOG.warn("Could not create category, topic and content without parameters");
		}
		return category;
	}
	
	/**
	 * Method to get a {@link Category} by its name and by a given {@link Topic} name.
	 * @param categoryName
	 * @param topicName
	 * @return the found {@link Category} or null
	 */
	public Category getCategoryByCategoryAndTopic(String categoryName, String topicName) {
		Category category = null;
		if (categoryName != null && topicName != null) {
			category = categoryService.getByName(categoryName);
			if (category != null) {
				Topic topic = topicService.getByName(topicName);
				if (topic == null) {
					LOG.warn("Could not get category with invalid topicName {]", topicName);
				}
			} else {
				LOG.warn("Could not get category with invalid categoryName {}", categoryName);
			}
		} else {
			LOG.warn("Could not get category without parameters");
		}
		return category;
	}
	
	/**
	 * <b>From DOKU:</b><br>
	 * in AIML 2.0, an attribute value can be specified by either an XML attribute value 
	 * or a subtag of the same name.
	 * @param node
	 * @param ps
	 * @param attributeName
	 * @return
	 */
	public String getAttributeOrTagValue(Node node, ParseState ps, String attributeName) {
		String result = null;
		//Retrieves a node (of any type) specified by name, 
		// or null if no node with the given name is found in the map
	    Node namedItem = node.getAttributes().getNamedItem(attributeName);
	    if(namedItem == null) {
	    	NodeList childList = node.getChildNodes();
	    	for(int i = 0; i < childList.getLength(); i++) {
	    		Node child = childList.item(i);
	    		if(child.getNodeName().equals(attributeName)) {
	    			//evaluate the contents of an AIML tag. calls recursEval on child tags.
	    			result = AIMLProcessor.evalTagContent(child, ps, null);
	    		}
	    	}
	    } else {
	    	result = namedItem.getNodeValue();
	    }
	    return result;
    }
}
