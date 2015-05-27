package de.fhws.hablame.chatbotbackend.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fhws.hablame.chatbotbackend.dto.ContentDTO;
import de.fhws.hablame.chatbotbackend.model.Content;
import de.fhws.hablame.chatbotbackend.model.Topic;
import de.fhws.hablame.chatbotbackend.repository.ContentRepository;
import de.fhws.hablame.chatbotbackend.repository.TopicRepository;

/**
 * Service class for the {@link ContentRepository}.
 * Here we handle all logic which is used to get the {@link Content} data from the database.
 */
@Service
public class ContentService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ContentService.class);

	@Autowired
	private ContentRepository contentRepository;
	@Autowired
	private TopicRepository topicRepository;
	
	/**
	 * Method to get all {@link Content}.
	 * @return a {@link List} of all existent {@link Content}
	 */
	public List<Content> getAllContent() {
		return contentRepository.findAll();
	}
	
	/**
	 * Method to get a single {@link Content} by its value.
	 * @param value
	 * @return the {@link Content} with the given value or null
	 */
	public Content getContentByValue(String value) {
		return contentRepository.findByValue(value);
	}
	
	/**
	 * Method to get {@link Content} by its id.
	 * @return
	 */
	public Content getContentById(Long id) {
		return contentRepository.findById(id);
	}
	
	/**
	 * Method to get all {@link Content} which are either <code>true</code> or <code>false</code>.
	 * @param multiple
	 * @return {@link List} of all {@link Content} which have the specific boolean value
	 */
	public List<Content> getContentsByMultiple(boolean multiple) {
		return contentRepository.findByMultiple(multiple);
	}
	
	/**
	 * Method to get the number of {@link Content}.
	 * @return
	 */
	public long countContent() {
		return contentRepository.count();
	}
	
	/**
	 * Method to create a {@link Content} with the given {@link ContentDTO} 
	 * and the corresponding {@link Topic} id.
	 * @param contentDTO
	 * @param topicId
	 * @return the created {@link Content} or null
	 */
	public Content createContent(Long topicId, ContentDTO contentDTO) {
		Content content = null;
		if (contentDTO != null && topicId != null) {
			Topic parentTopic = topicRepository.findById(topicId);
			if (parentTopic != null) {
				if (contentRepository.findByValue(contentDTO.getValue()) == null) {
					content = new Content();
					content.setActive(contentDTO.isActive());
					content.setCreateTime(new Date());
					content.setMultiple(contentDTO.isMultiple());
					content.setTopic(parentTopic);
					content.setValue(contentDTO.getValue());
					content = contentRepository.save(content);
					LOG.info("Created content with value {}", content.getValue());
				} else {
					LOG.warn("Could not create content with already existent value {}", contentDTO.getValue());
				}
			} else {
				LOG.warn("Could not create category with invalid topicId {}", topicId);
			}
		} else {
			LOG.warn("Could not create category without DTO and topicId");
		}
		return content;
	}
	
	/**
	 * Method to delete a {@link Content} by its value.
	 * @param value
	 */
	public void deleteContentByValue(String value) {
		if (value != null) {
			contentRepository.delete(contentRepository.findByValue(value));
			LOG.info("Deleted content with value {}", value);
		} else {
			LOG.warn("Could not delete content without value");
		}
	}
}
