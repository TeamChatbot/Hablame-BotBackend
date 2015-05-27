package de.fhws.hablame.chatbotbackend.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhws.hablame.chatbotbackend.service.ThreadService;

public class ChatbotThread extends Thread implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChatbotThread.class);
	
	private String name;
	private ThreadService threadService;
	
	public ChatbotThread(String name, ThreadService threadService) {
		this.name = name;
		this.threadService = threadService;
	}

	@Override
	public void run() {
		LOG.info("Conversation Thread: "+name+" started");
		while(ThreadService.getRun()) {
			try {
				//just to get rid of the unusage warning...
				threadService.stopConversationThread();
				LOG.info("Conversation Thread: "+name+" sleeps");
				Thread.sleep(5000);
			} catch (InterruptedException ie) {
				LOG.info("Conversation Thread: "+name+" got interrupted");
			}
		}
		LOG.info("Conversation Thread: "+name+" stopped");
	}

}
