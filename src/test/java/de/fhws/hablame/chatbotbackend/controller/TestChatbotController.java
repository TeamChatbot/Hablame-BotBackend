package de.fhws.hablame.chatbotbackend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Rollback(value=true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:spring/spring-context-test.xml")
/**
 * Testclass for the {@link ChatbotController}.
 */
public class TestChatbotController {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private static final String PERFORM_REQUEST_ERROR = "Could not perform request";
	private static final String CONTROLLER_CHATBOT_NOT_STOPPED = "chatbot could not be stopped";
	
	private MockMvc mockMvc;
	private ObjectMapper mapper;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	@Test
	public void testConversation() {
		try {
			mockMvc.perform(post(ChatbotController.CONVERSATION)
					.accept(MediaType.TEXT_PLAIN_VALUE)
					//if you want to send objects in the body just use the objectmapper
					//e.g.: mapper.writeValueAsBytes(object)
					.content("hallo")
					.contentType(MediaType.TEXT_PLAIN_VALUE)
					.locale(Locale.GERMAN))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE))
			.andDo(print());
			//TODO: expect exact content
		} catch (Exception e1) {
			e1.printStackTrace();
			AssertionErrors.fail(PERFORM_REQUEST_ERROR);
		}
	}
	
	@Test
	public void testStop() {
		try {
			mockMvc.perform(get(ChatbotController.STOPCONVERSATION)
					.accept(MediaType.TEXT_PLAIN_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE))
			.andExpect(content().string(CONTROLLER_CHATBOT_NOT_STOPPED))
			.andDo(print());
		} catch (Exception e) {
			e.printStackTrace();
			AssertionErrors.fail(PERFORM_REQUEST_ERROR);
		}
	}
}
