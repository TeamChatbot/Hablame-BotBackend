package de.fhws.hablame.chatbotbackend.dto;

import java.util.List;

/**
 * DTO for the {@link de.fhws.hablame.chatbotbackend.model.Topic} model class.
 */
public class TopicDTO {

	private String name;
	
	private Boolean active;
	
	private Long categoryId;
	
	private List<Long> contentIds;

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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<Long> getContentIds() {
		return contentIds;
	}

	public void setContentIds(List<Long> contentIds) {
		this.contentIds = contentIds;
	}
}
