package de.fhws.hablame.chatbotbackend.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
//TODO: update spring so we can annotate this here
//@Rollback(value=true)
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
		//TODO
	}
	
	@Rollback(value=true)
	@Test
	public void testCategoryService() {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setActive(true);
		categoryDTO.setName(categoryName);
		Category category = categoryService.create(categoryDTO);
		assertNotNull(category);
		Category category2 = categoryService.getById(category.getId());
		assertNotNull(category2);
		assertEquals(category.getName(), category2.getName());
		category2 = categoryService.getByName(category.getName());
		assertNotNull(category2);
		assertEquals(category.getName(), category2.getName());
		List<Category> categories = categoryService.getAll();
		assertEquals(categories.size(), categoryService.count());
		categoryService.deleteCategoryByName(category.getName());
		assertNull(categoryService.getById(category.getId()));
	}
	
	@Rollback(value=true)
	@Test
	public void testTopicService() {
		TopicDTO topicDTO = new TopicDTO();
		topicDTO.setActive(true);
		topicDTO.setName(topicName);
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setActive(true);
		categoryDTO.setName(categoryName);
		Category category = categoryService.create(categoryDTO);
		Topic topic = topicService.createTopic(category.getId(), topicDTO);
		assertNotNull(topic);
		Topic topic2 = topicService.getTopicById(topic.getId());
		assertNotNull(topic2);
		assertEquals(topic.getName(), topic2.getName());
		topic2 = topicService.getTopicByName(topic.getName());
		assertNotNull(topic2);
		assertEquals(topic.getName(), topic2.getName());
		List<Topic> topics = topicService.getAllTopics();
		assertEquals(topics.size(), topicService.countTopics());
		topicService.deleteTopicByName(topic.getName());
		assertNull(topicService.getTopicById(topic.getId()));
		categoryService.deleteCategoryByName(category.getName());
		assertNull(categoryService.getById(category.getId()));
	}

	@Rollback(value=true)
	@Test
	public void testContentService() {
		ContentDTO contentDTO = new ContentDTO();
		contentDTO.setActive(true);
		contentDTO.setMultiple(false);
		contentDTO.setValue(contentValue);
		TopicDTO topicDTO = new TopicDTO();
		topicDTO.setActive(true);
		topicDTO.setName(topicName);
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setActive(true);
		categoryDTO.setName(categoryName);
		Category category = categoryService.create(categoryDTO);
		Topic topic = topicService.createTopic(category.getId(), topicDTO);
		Content content = contentService.createContent(topic.getId(), contentDTO);
		assertNotNull(content);
		Content content2 = contentService.getContentById(content.getId());
		assertNotNull(content2);
		assertEquals(content.getValue(), content2.getValue());
		content2 = contentService.getContentByValue(content.getValue());
		assertNotNull(content2);
		assertEquals(content.getValue(), content2.getValue());
		List<Content> contents = contentService.getAllContent();
		assertEquals(contents.size(), contentService.countContent());
		contentService.deleteContentByValue(content.getValue());
		assertNull(contentService.getContentById(content.getId()));
		topicService.deleteTopicByName(topic.getName());
		assertNull(topicService.getTopicById(topic.getId()));
		categoryService.deleteCategoryByName(category.getName());
		assertNull(categoryService.getById(category.getId()));
	}
}
