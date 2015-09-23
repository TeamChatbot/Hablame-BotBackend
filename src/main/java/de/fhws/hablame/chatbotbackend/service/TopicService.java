package de.fhws.hablame.chatbotbackend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
public class TopicService implements IService<TopicDTO, Topic> {
	
	private static final Logger LOG = LoggerFactory.getLogger(TopicService.class);

	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	/**
	 * Method to get all {@link Topic}.
	 * @return {@link List} with all existent {@link Topic}
	 */
	public List<Topic> getAll() {
		return topicRepository.findAll();
	}
	
	/**
	 * Method to get a specific {@link Topic} by its name.
	 * @param topicName
	 * @return the found {@link Topic} or null
	 */
	public Topic getByName(String topicName) {
		return topicRepository.findByName(topicName);
	}
	
	/**
	 * Method to get a specific {@link Topic} by its its.
	 * @param id
	 * @return the found {@link Topic} or null
	 */
	public Topic getById(Long id) {
		return topicRepository.findOne(id);
	}
	
	/**
	 * Method to get a {@link List} of {@link Topic}s by a given List of ids
	 * @param ids
	 * @return List of Topics or empty list
	 */
	public List<Topic> getByIds(List<Long> ids) {
		List<Topic> topics = new ArrayList<Topic>();
		if (ids != null && !ids.isEmpty()) {
			Iterator<Topic> topicIter = topicRepository.findAll(ids).iterator();
			while(topicIter.hasNext()) {
				topics.add(topicIter.next());
			}
		} else {
			LOG.warn("Could not fetch Topics without ids");
		}
		return topics;
	}
	
	/**
	 * Method to get the number of {@link Topic}.
	 * @return
	 */
	public long count() {
		return topicRepository.count();
	}
	
	/**
	 * Method to create a {@link Topic} by a given {@link Category} id and a {@link TopicDTO}.
	 * @param topicDTO
	 * @return the created {@link Topic} or null
	 */
	public Topic create(TopicDTO topicDTO) {
		Topic topic = null;
		if (topicDTO != null) {
			if (topicRepository.findByName(topicDTO.getName()) == null) {
				topic = new Topic();
				topic.setActive(topicDTO.isActive());
				if (topicDTO.getCategoryId() != null) {
					Category category = 
							categoryRepository.findOne(topicDTO.getCategoryId());
					if (category != null) {
						topic.setCategory(category);
					} else {
						LOG.debug("Topic with invalid categoryId {}", 
								topicDTO.getCategoryId());
					}
				} else {
					LOG.debug("Topic without categoryId");
				}
				topic.setCreateTime(new Date());
				topic.setName(topicDTO.getName());
				topic = topicRepository.save(topic);
				LOG.info("Created topic with name {}", topic.getName());
			} else {
				LOG.warn("Could not create topic with already existent name {}", topicDTO.getName());
			}
		} else {
			LOG.warn("Could not create topic without DTO");
		}
		return topic;
	}
	
	/**
	 * Method to delete a {@link Topic} by its name.
	 * @param name
	 * @return true if Topic could be deleted and false otherwise
	 */
	public Boolean deleteByName(String name) {
		if (name != null) {
			Topic topic = topicRepository.findByName(name);
			if (topic != null) {
				topicRepository.delete(topic);
				LOG.info("Deleted topic with name {}", name);
				return true;
			} else {
				LOG.warn("Could not delete Topic with invalid name {}", name);
			}
		} else {
			LOG.warn("Could not delete topic without name");
		}
		return false;
	}

	/**
	 * Method to delete a {@link Topic} by its id
	 * @param id
	 * @return true if Topic could be deleted and false otherwise
	 */
	public Boolean deleteById(Long id) {
		if (id != null) {
			if (topicRepository.findOne(id) != null) {
				topicRepository.delete(id);
				LOG.info("Deleted Topic with id {}", id);
				return true;
			} else {
				LOG.warn("Could not delete Topic with invalid id {}", id);
			}
		} else {
			LOG.warn("Could not delete Topic without id");
		}
		return false;
	}
}
