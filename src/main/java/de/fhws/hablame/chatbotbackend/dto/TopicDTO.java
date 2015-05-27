package de.fhws.hablame.chatbotbackend.dto;

/**
 * DTO for the {@link de.fhws.hablame.chatbotbackend.model.Topic} model class.
 */
public class TopicDTO {

	private String name;
	
	private boolean active;

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
}
