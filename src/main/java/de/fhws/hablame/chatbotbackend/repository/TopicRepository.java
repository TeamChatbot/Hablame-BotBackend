package de.fhws.hablame.chatbotbackend.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Content;
import de.fhws.hablame.chatbotbackend.model.Topic;

/**
 * {@link Repository} class for the {@link Topic} model.
 * This interface handles all persistence of this object with ORM.
 * @see http://docs.spring.io/spring-data/jpa/docs/1.6.4.RELEASE/reference/html/jpa.repositories.html
 */
@Repository
public interface TopicRepository extends PagingAndSortingRepository<Topic, Long> {

	public List<Topic> findAll();
	
	public Topic findByName(String name);
	
	public List<Topic> findByContent(Content content);
	
	public Topic findByCategory(Category category);
}
