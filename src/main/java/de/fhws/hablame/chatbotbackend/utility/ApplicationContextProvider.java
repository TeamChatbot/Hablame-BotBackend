package de.fhws.hablame.chatbotbackend.utility;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This little helper will get you every bean context from anywhere.
 * Just call the {@link #getApplicationContext()}.getBean("BeanId", Bean.class)
 * @author David Artmann
 *
 */
public class ApplicationContextProvider implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	public static ApplicationContext getApplicationContext() {
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		context = ctx;
	}

}
