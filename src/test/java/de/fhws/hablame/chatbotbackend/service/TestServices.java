package de.fhws.hablame.chatbotbackend.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.fhws.hablame.chatbotbackend.dto.CategoryDTO;
import de.fhws.hablame.chatbotbackend.dto.ContentDTO;
import de.fhws.hablame.chatbotbackend.dto.TopicDTO;
import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.model.Content;
import de.fhws.hablame.chatbotbackend.model.Topic;

/**
 * This is the Testclass for the {@link Service} annotated classes from the project.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/spring-context.xml")
public class TestServices {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private DataSource dataSource;
	
	/**
	 * This method runs after each JUnit test and calls the {@link DataSource},
	 * which is gets the datasource from the spring-context.xml and the 
	 * {@link JdbcTemplate} uses it, to execute sql statements.
	 * So when an test failes, we make sure, that all data is deleted from the db.
	 */
	//TODO: do the testing in an extra database ("chatbot_test")!!!
	@After
	public void cleanUp() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("DELETE FROM CONTENT");
		jdbcTemplate.execute("DELETE FROM TOPIC");
		jdbcTemplate.execute("DELETE FROM CATEGORY");
	}
	
	@Test
	public void testCategoryService() {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setActive(true);
		categoryDTO.setName("testCategory");
		Category category = categoryService.createCategory(categoryDTO);
		assertNotNull(category);
		Category category2 = categoryService.getCategoryById(category.getId());
		assertNotNull(category2);
		assertEquals(category.getName(), category2.getName());
		category2 = categoryService.getCategoryByName(category.getName());
		assertNotNull(category2);
		assertEquals(category.getName(), category2.getName());
		List<Category> categories = categoryService.getAllCategories();
		assertEquals(categories.size(), categoryService.countCategories());
		categoryService.deleteCategoryByName(category.getName());
		assertNull(categoryService.getCategoryById(category.getId()));
	}
	
	@Test
	public void testTopicService() {
		TopicDTO topicDTO = new TopicDTO();
		topicDTO.setActive(true);
		topicDTO.setName("testTopic");
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setActive(true);
		categoryDTO.setName("testCategory");
		Category category = categoryService.createCategory(categoryDTO);
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
		assertNull(categoryService.getCategoryById(category.getId()));
	}

	@Test
	public void testContentService() {
		ContentDTO contentDTO = new ContentDTO();
		contentDTO.setActive(true);
		contentDTO.setMultiple(false);
		contentDTO.setValue("testValue");
		TopicDTO topicDTO = new TopicDTO();
		topicDTO.setActive(true);
		topicDTO.setName("testTopic");
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setActive(true);
		categoryDTO.setName("testCategory");
		Category category = categoryService.createCategory(categoryDTO);
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
		assertNull(categoryService.getCategoryById(category.getId()));
	}
}
