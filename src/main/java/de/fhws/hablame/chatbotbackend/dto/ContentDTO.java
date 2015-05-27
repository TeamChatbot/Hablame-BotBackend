package de.fhws.hablame.chatbotbackend.dto;

/**
 * DTO class for the {@link de.fhws.hablame.chatbotbackend.model.Content} model class.
 */
public class ContentDTO {
	
	private String value;
	
	private boolean multiple;
	
	private boolean active;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
