package de.fhws.hablame.chatbotbackend.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Topic;

/**
 * {@link Repository} class for the {@link Category} model.
 * This interface handles all persistence of this object with ORM.
 * @see http://docs.spring.io/spring-data/jpa/docs/1.6.4.RELEASE/reference/html/jpa.repositories.html
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

	public List<Category> findAll();
	
	public Category findByName(String name);
	
	public List<Category> findByTopic(Topic topic);
}
