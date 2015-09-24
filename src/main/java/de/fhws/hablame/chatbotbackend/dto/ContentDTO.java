package de.fhws.hablame.chatbotbackend.dto;

/**
 * DTO class for the {@link de.fhws.hablame.chatbotbackend.model.Content} model class.
 */
public class ContentDTO {
	
	private String value;
	
	private Boolean multiple;
	
	private Boolean active;
	
	private Long topicId;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getMultiple() {
		return multiple;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
}
