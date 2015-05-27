package de.fhws.hablame.chatbotbackend.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import de.fhws.hablame.chatbotbackend.model.Content;

/**
 * {@link Repository} class for the {@link Content} model.
 * This interface handles all persistence of this object with ORM.
 * @see http://docs.spring.io/spring-data/jpa/docs/1.6.4.RELEASE/reference/html/jpa.repositories.html
 */
@Repository
public interface ContentRepository extends PagingAndSortingRepository<Content, Long> {

	public List<Content> findAll();
	
	public Content findById(Long id);
	
	public Content findByValue(String value);
	
	public List<Content> findByMultiple(boolean multiple);
}
