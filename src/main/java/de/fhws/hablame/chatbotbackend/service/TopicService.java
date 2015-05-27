package de.fhws.hablame.chatbotbackend.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fhws.hablame.chatbotbackend.dto.TopicDTO;
import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Topic;
import de.fhws.hablame.chatbotbackend.repository.CategoryRepository;
import de.fhws.hablame.chatbotbackend.repository.TopicRepository;

/**
 * Service class for the {@link TopicRepository}.
 * Here we handle all logic which is used to get the {@link Topic} data from the database.
 */
@Service
public class TopicService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TopicService.class);

	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	/**
	 * Method to get all {@link Topic}.
	 * @return {@link List} with all existent {@link Topic}
	 */
	public List<Topic> getAllTopics() {
		return topicRepository.findAll();
	}
	
	/**
	 * Method to get a specific {@link Topic} by its name.
	 * @param topicName
	 * @return the found {@link Topic} or null
	 */
	public Topic getTopicByName(String topicName) {
		return topicRepository.findByName(topicName);
	}
	
	/**
	 * Method to get a specific {@link Topic} by its its.
	 * @param id
	 * @return the found {@link Topic} or null
	 */
	public Topic getTopicById(Long id) {
		return topicRepository.findById(id);
	}
	
	/**
	 * Method to get the number of {@link Topic}.
	 * @return
	 */
	public long countTopics() {
		return topicRepository.count();
	}
	
	/**
	 * Method to create a {@link Topic} by a given {@link Category} id and a {@link TopicDTO}.
	 * @param categoryId
	 * @param topicDTO
	 * @return the created {@link Topic} or null
	 */
	public Topic createTopic(Long categoryId, TopicDTO topicDTO) {
		Topic topic = null;
		if (categoryId != null && topicDTO != null) {
			Category category = categoryRepository.findById(categoryId);
			if (category != null) {
				if (topicRepository.findByName(topicDTO.getName()) == null) {
					topic = new Topic();
					topic.setActive(topicDTO.isActive());
					topic.setCategory(category);
					topic.setCreateTime(new Date());
					topic.setName(topicDTO.getName());
					topic = topicRepository.save(topic);
					LOG.info("Created topic with name {}", topic.getName());
				} else {
					LOG.warn("Could not create topic with already existent name {}", topicDTO.getName());
				}
			} else {
				LOG.warn("Could not create topic with invalid categoryId {}", categoryId);
			}
		} else {
			LOG.warn("Could not create topic without DTO and categoryId");
		}
		return topic;
	}
	
	/**
	 * Method to delete a {@link Topic} by its name.
	 * @param topicName
	 */
	public void deleteTopicByName(String topicName) {
		if (topicName != null) {
			topicRepository.delete(topicRepository.findByName(topicName));
			LOG.info("Deleted topic with name {}", topicName);
		} else {
			LOG.warn("Could not delete topic without name");
		}
	}
}
