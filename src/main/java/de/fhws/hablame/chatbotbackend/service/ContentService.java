package de.fhws.hablame.chatbotbackend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fhws.hablame.chatbotbackend.dto.ContentDTO;
import de.fhws.hablame.chatbotbackend.model.Content;
import de.fhws.hablame.chatbotbackend.model.Topic;
import de.fhws.hablame.chatbotbackend.repository.ContentRepository;

/**
 * Service class for the {@link ContentRepository}.
 * Here we handle all logic which is used to get the {@link Content} data from the database.
 */
@Service
public class ContentService implements IService<ContentDTO, Content> {
	
	private static final Logger LOG = LoggerFactory.getLogger(ContentService.class);

	@Autowired
	private ContentRepository contentRepository;
	@Autowired
	private TopicService topicService;
	
	/**
	 * Method to get all {@link Content}.
	 * @return a {@link List} of all existent {@link Content}
	 */
	public List<Content> getAll() {
		return contentRepository.findAll();
	}
	
	/**
	 * Method to get a single {@link Content} by its value.
	 * @param value
	 * @return the {@link Content} with the given value or null
	 */
	public Content getByValue(String value) {
		return contentRepository.findByValue(value);
	}
	
	/**
	 * Method to get {@link Content} by its id.
	 * @return
	 */
	public Content getById(Long id) {
		return contentRepository.findOne(id);
	}
	
	/**
	 * Method to get all {@link Content}s by a given {@link List} of ids
	 * @param ids
	 * @return List of Content or empty list
	 */
	public List<Content> getByIds(List<Long> ids) {
		List<Content> contents = new ArrayList<>();
		if (ids != null) {
			Iterator<Content> contentIter = contentRepository.findAll(ids).iterator();
			while(contentIter.hasNext()) {
				contents.add(contentIter.next());
			}
		} else {
			LOG.warn("Could not fetch Contents without ids");
		}		
		return contents;
	}
	
	/**
	 * Method to get all {@link Content} which are either <code>true</code> or <code>false</code>.
	 * @param multiple
	 * @return {@link List} of all {@link Content} which have the specific boolean value
	 */
	public List<Content> getByMultiple(Boolean multiple) {
		return contentRepository.findByMultiple(multiple);
	}
	
	/**
	 * Method to get the number of {@link Content}.
	 * @return
	 */
	public long count() {
		return contentRepository.count();
	}
	
	/**
	 * Method to create a {@link Content} with a given {@link ContentDTO}
	 * @param contentDTO
	 * @return the created {@link Content} or null
	 */
	public Content create(ContentDTO contentDTO) {
		Content content = null;
		if (contentDTO != null) {
			if (contentRepository.findByValue(contentDTO.getValue()) == null) {
				content = new Content();
				content.setActive(contentDTO.getActive());
				content.setCreateTime(new Date());
				content.setMultiple(contentDTO.getMultiple());
				if (contentDTO.getTopicId() != null) {
					Topic topic = topicService.getById(contentDTO.getTopicId());
					if (topic != null) {
						content.setTopic(topic);
					} else {
						LOG.debug("ContentDTO with invalid topicId {}",
								contentDTO.getTopicId());
					}					
				} else {
					LOG.debug("ContentDTO without topicId");
				}				
				content.setValue(contentDTO.getValue());
				content = contentRepository.save(content);
				LOG.info("Created content with value {}", content.getValue());
			} else {
				LOG.warn("Could not create Content with already existent value {}", contentDTO.getValue());
			}
		} else {
			LOG.warn("Could not create Content without ContentDTO");
		}
		return content;
	}
	
	/**
	 * Method to delete a {@link Content} by its value.
	 * @param value
	 * @return true if content could be deleted and false otherwise
	 */
	public Boolean deleteByValue(String value) {
		if (value != null) {
			Content content = contentRepository.findByValue(value);
			if (content != null) {
				contentRepository.delete(content);
				LOG.info("Deleted content with value {}", value);
				return true;
			} else {
				LOG.warn("Could not delete content with invalid value {}", value);
			}
		} else {
			LOG.warn("Could not delete content without value");
		}
		return false;
	}

	/**
	 * Method to delete a {@link Content} by a given id
	 * @param id
	 * @return true if content could be deleted and false otherwise
	 */
	public Boolean deleteById(Long id) {
		if (id != null) {
			Content content = contentRepository.findOne(id);
			if (content != null) {
				contentRepository.delete(id);
				LOG.info("Deleted content with id {}", id);
				return true;
			} else {
				LOG.warn("Could not delete content with invalid id {}", id);
			}
		} else {
			LOG.warn("Could not delete content without id");
		}
		return false;
	}
}
