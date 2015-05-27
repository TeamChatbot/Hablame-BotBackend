package de.fhws.hablame.chatbotbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.fhws.hablame.chatbotbackend.service.chatbot.ChatbotService;

/**
 * {@link RestController} annotated class which handles the webrequests like GET and POST.
 * A controller has {@link Service} annotated classes injected in his context with {@link Autowired}.
 */
@RestController
public class ChatbotController {
	
	public static final String MAPPING = "/conversation";
	
	@Autowired
	private ChatbotService chatbotService;
	
	@RequestMapping(value=MAPPING, method=RequestMethod.POST)
	@ResponseBody
	public String startConversationByRequestBody(@RequestBody String message) {
		return chatbotService.init(message);
	}
}
