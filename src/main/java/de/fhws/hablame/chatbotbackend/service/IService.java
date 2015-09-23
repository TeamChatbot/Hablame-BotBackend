package de.fhws.hablame.chatbotbackend.service;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Interface for the {@link CategoryService}, {@link TopicService} and 
 * {@link ContentService}. Each new {@link Service} should implement this Interface
 * if it wraps a model class with a {@link Repository}.
 */
public interface IService <D, M> {

	public long count();
	
	public M create(D d);
	
	public Boolean deleteById(Long id);
	
	public List<M> getAll();
	
	public M getById(Long id);
}
