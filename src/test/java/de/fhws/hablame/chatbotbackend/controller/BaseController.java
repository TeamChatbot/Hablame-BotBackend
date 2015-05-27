package de.fhws.hablame.chatbotbackend.controller;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import de.fhws.hablame.chatbotbackend.service.chatbot.ChatbotService;

/**
 * The basic class for all controller tests. It mocks all necessary services before the test are running.
 * Additionally it holds an {@link MockMvc} for the controller test.
 */
public class BaseController {
	
	/**
	 * The error message for a failed controller test
	 */
	protected static final String PERFORM_REQUEST_ERROR = "Could not perform request";

	/**
	 * Entry point for the spring controller tests.
	 */
	protected MockMvc mockMvc;
	
	
	/**
	 * Because the {@link ChatbotService} is used by the {@link ChatbotController}
	 * we need to mock it before the testing.
	 */
	@Mock
	private ChatbotService chatbotService;
	
	@Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}
