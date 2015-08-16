package de.fhws.hablame.chatbotbackend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Testclass for the {@link ChatbotController}.
 */
public class TestChatbotController extends BaseController {

	@InjectMocks
	private ChatbotController chatbotController;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(chatbotController).build();
	}
	
	@Test
	public void testController() {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(ChatbotController.CONVERSATION)
				.accept(MediaType.TEXT_PLAIN)
				.content("hallo alice")
				.contentType(MediaType.TEXT_PLAIN)
				.locale(Locale.GERMAN);
		try {
			mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk()).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
			AssertionErrors.fail(PERFORM_REQUEST_ERROR);
		}
	}
}
