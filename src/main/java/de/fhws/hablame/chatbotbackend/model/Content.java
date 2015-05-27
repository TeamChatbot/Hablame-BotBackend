package de.fhws.hablame.chatbotbackend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model class for a content. Which is logically under the topic and the most below element in AIML.
 * A content has one topic as parent.
 */
@Entity
@Table(name="content", uniqueConstraints={
		@UniqueConstraint(columnNames={"value", "topic_id"}, name="value_and_topic_id")})
@JsonIgnoreProperties(ignoreUnknown=true)
public class Content extends MappedSuperClass{

	@Transient
	private static final long serialVersionUID = 3731352147353279522L;
	
	@Column(unique=true, nullable=false)
	private String value;
	
	@Column(nullable=false)
	private boolean multiple;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="topic_id", nullable=false)
	@JsonBackReference
	private Topic topic;

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
