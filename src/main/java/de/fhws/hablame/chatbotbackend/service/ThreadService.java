package de.fhws.hablame.chatbotbackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import de.fhws.hablame.chatbotbackend.service.chatbot.ChatbotService;
import de.fhws.hablame.chatbotbackend.thread.ChatbotThread;

/**
 * {@link Service} class for the Chatbot communication.
 * This service class can start and stop the {@link ChatbotThread}.
 * TODO: put logic from this class into the {@link ChatbotService}.
 */
@Service
public class ThreadService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ThreadService.class);

	private static volatile Boolean run = false;
	
	private ApplicationContext applicationContext;
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	private int numberOfChatbotThreads = 0;
	
	public void startConversationThread() {
		//access the thread-context file within the classpath:"resources" folder, 
		//because I had no success locating the file in the WEB-INF/spring folder
		applicationContext = new ClassPathXmlApplicationContext("spring/spring-thread.xml");
		threadPoolTaskExecutor = (ThreadPoolTaskExecutor) applicationContext.getBean("taskExecutor");
		setRun(true);
		threadPoolTaskExecutor.execute(new ChatbotThread(ChatbotThread.class.getSimpleName()+numberOfChatbotThreads, this));
		numberOfChatbotThreads++;
	}
	
	public void stopConversationThread() {
		if (threadPoolTaskExecutor != null) {
			if (threadPoolTaskExecutor.getActiveCount() > 0) {
				setRun(false);
				threadPoolTaskExecutor.shutdown();
				((ConfigurableApplicationContext) applicationContext).close();
			} else {
				LOG.warn("TaskExecutor has not active threads");
			}
		}
	}

	public static Boolean getRun() {
		return run;
	}

	public static void setRun(Boolean run) {
		ThreadService.run = run;
	}
	
}
