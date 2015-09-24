package de.fhws.hablame.chatbotbackend.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.fhws.hablame.chatbotbackend.dto.CategoryDTO;
import de.fhws.hablame.chatbotbackend.dto.ContentDTO;
import de.fhws.hablame.chatbotbackend.dto.TopicDTO;
import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Content;
import de.fhws.hablame.chatbotbackend.model.Topic;

/**
 * This is the Testclass for the {@link Service} annotated classes from the project.
 */
@Rollback(value=true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/spring-context.xml")
public class TestServices {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private ContentService contentService;
	
	private CategoryDTO categoryDTO;
	private TopicDTO topicDTO;
	private ContentDTO contentDTO;
	private Category category;
	private Topic topic;
	private Content content;
	private String categoryName = "testCategory";
	private String topicName = "testTopic";
	private String contentValue = "testValue";
	
	@Before
	public void setup() {
		contentDTO = new ContentDTO();
		contentDTO.setActive(true);
		contentDTO.setMultiple(false);
		contentDTO.setValue(contentValue);
		topicDTO = new TopicDTO();
		topicDTO.setActive(true);
		topicDTO.setName(topicName);
		categoryDTO = new CategoryDTO();
		categoryDTO.setActive(true);
		categoryDTO.setName(categoryName);
		
	}
	
	@Test
	public void testCategoryService() {
		category = categoryService.create(categoryDTO);
		assertNotNull(category);
		Category category2 = categoryService.getById(category.getId());
		assertNotNull(category2);
		assertEquals(category.getName(), category2.getName());
		category2 = categoryService.getByName(category.getName());
		assertNotNull(category2);
		assertEquals(category.getName(), category2.getName());
		List<Category> categories = categoryService.getAll();
		assertEquals(categories.size(), categoryService.count());
		categoryService.deleteByName(category.getName());
		assertNull(categoryService.getById(category.getId()));
		assertFalse(categoryService.deleteById(category.getId()));
	}
	
	@Test
	public void testTopicService() {
		category = categoryService.create(categoryDTO);
		topicDTO.setCategoryId(category.getId());
		topic = topicService.create(topicDTO);
		assertNotNull(topic);
		Topic topic2 = topicService.getById(topic.getId());
		assertEquals(topic.getName(), topic2.getName());
		topic2 = topicService.getByName(topic.getName());
		assertEquals(topic.getName(), topic2.getName());
		List<Topic> topics = topicService.getAll();
		assertEquals(topics.size(), topicService.count());
		List<Long> ids = new ArrayList<>();
		ids.add(topic.getId());
		ids.add(topic2.getId());
		assertEquals(topicService.getByIds(ids).size(), topicService.count());
		topicService.deleteByName(topic.getName());
		assertNull(topicService.getById(topic.getId()));
		assertFalse(topicService.deleteById(topic.getId()));
		categoryService.deleteByName(category.getName());
		assertNull(categoryService.getById(category.getId()));
	}

	@Test
	public void testContentService() {
		category = categoryService.create(categoryDTO);
		topicDTO.setCategoryId(category.getId());
		topic = topicService.create(topicDTO);
		contentDTO.setTopicId(topic.getId());
		content = contentService.create(contentDTO);
		topic.setContents(contentService.getAll());
		assertNotNull(content);
		Content content2 = contentService.getById(content.getId());
		assertNotNull(content2);
		assertEquals(content.getValue(), content2.getValue());
		content2 = contentService.getByValue(content.getValue());
		assertNotNull(content2);
		assertEquals(content.getValue(), content2.getValue());
		List<Content> contents = contentService.getAll();
		assertEquals(contents.size(), contentService.count());
		contentService.deleteByValue(content.getValue());
		assertNull(contentService.getById(content.getId()));
		topicService.deleteByName(topic.getName());
		assertNull(topicService.getById(topic.getId()));
		categoryService.deleteByName(category.getName());
		assertNull(categoryService.getById(category.getId()));
	}
}
