package de.fhws.hablame.chatbotbackend.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Content;
import de.fhws.hablame.chatbotbackend.model.Topic;

/**
 * This is the Testclass for the {@link Repository} annotated classes from the project.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/spring-context.xml")
public class TestRepositories {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private ContentRepository contentRepository;
	
	@Test
	public void testCategoryRepository() {
		List<Category> categories = categoryRepository.findAll();
		assertNotNull(categories);
		assertEquals(categoryRepository.count(), categories.size());
	}
	
	@Test
	public void testTopicRepository() {
		List<Topic> topics = topicRepository.findAll();
		assertNotNull(topics);
		assertEquals(topicRepository.count(), topics.size());
	}
	
	@Test
	public void TestContentRepository() {
		List<Content> contents = contentRepository.findAll();
		assertNotNull(contents);
		assertEquals(contentRepository.count(), contents.size());
	}
}
