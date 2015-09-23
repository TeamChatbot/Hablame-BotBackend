package de.fhws.hablame.chatbotbackend.dto;

import java.util.List;

/**
 * DTO for the {@link de.fhws.hablame.chatbotbackend.model.Category} model class.
 */
public class CategoryDTO {

	private String name;
	
	private boolean active;
	
	private List<Long> topicIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Long> getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(List<Long> topicIds) {
		this.topicIds = topicIds;
	}
	
}
