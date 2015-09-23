package de.fhws.hablame.chatbotbackend.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Content;
import de.fhws.hablame.chatbotbackend.model.Topic;

/**
 * This is the Testclass for the {@link Repository} annotated classes from the project.
 */
//TODO: update spring so we can annotate this here
//@Rollback(value=true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/spring-context-test.xml")
public class TestRepositories {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private ContentRepository contentRepository;
	
	private Category category;
	private Topic topic;
	private Content content;
	private String categoryName = "test category";;
	private String topicName = "test topic";
	private String contentValue = "test value";
	private Boolean contentMultiple = false;
	
	@Before
	public void setup() {
		content = new Content();
		content.setActive(true);
		content.setCreateTime(new Date());
		content.setMultiple(contentMultiple);
		content.setValue(contentValue);
		content = contentRepository.save(content);
		topic = new Topic();
		topic.setActive(true);
		topic.setCreateTime(new Date());
		topic.setName(topicName);
		topic = topicRepository.save(topic);
		category = new Category();
		category.setActive(true);
		category.setCreateTime(new Date());
		category.setName(categoryName);
		List<Topic> topics = new ArrayList<Topic>();
		topics.add(topic);
		category.setTopic(topics);
		category = categoryRepository.save(category);
		topic.setCategory(category);
		List<Content> contents = new ArrayList<Content>();
		contents.add(content);
		topic.setContent(contents);
		content.setTopic(topic);
		
	}
	
	@Rollback(value=true)
	@Test
	public void testCategoryRepository() {
		assertNotNull(categoryRepository.findOne(category.getId()));
		assertNotNull(categoryRepository.findByName(categoryName));
		assertEquals(categoryRepository.findByTopic(topic).iterator().next().getId(), 
				category.getId());
		List<Category> categories = categoryRepository.findAll();
		assertEquals(categoryRepository.count(), categories.size());
	}
	
	@Rollback(value=true)
	@Test
	public void testTopicRepository() {
		assertEquals(topic.getId(), topicRepository.findOne(topic.getId()).getId());
		assertEquals(topic.getId(), topicRepository.findByName(topicName).getId());
		assertEquals(topic.getId(), 
				topicRepository.findByCategory(category).getId());
		assertEquals(topic.getId(), 
				topicRepository.findByContent(content).iterator().next().getId());
		List<Topic> topics = topicRepository.findAll();
		assertEquals(topics.size(), topicRepository.count());
	}
	
	@Rollback(value=true)
	@Test
	public void TestContentRepository() {
		assertEquals(content.getId(), contentRepository.findOne(content.getId()).getId());
		assertEquals(content.getId(), contentRepository.findByValue(contentValue).getId());
		assertEquals(content.getId(), 
				contentRepository.findByMultiple(
						contentMultiple).iterator().next().getId());
		assertEquals(content.getId(), 
				contentRepository.findByTopic(topic).iterator().next().getId());
		List<Content> contents = contentRepository.findAll();
		assertNotNull(contents);
		assertEquals(contents.size(), contentRepository.count());
	}
}
