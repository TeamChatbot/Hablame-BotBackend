package de.fhws.hablame.chatbotbackend.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Model class for a category. Which are the logically highest element of an AIML element.
 * A category has one or more topics as childs.
 */
@Entity
@Table(name="category")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Category extends MappedSuperClass {
	
	//TODO: rethink the association to topic, maybe a n:m relation?
	
	@Transient
	private static final long serialVersionUID = 167235400647608771L;

	@Column(unique=true, nullable=false)
	private String name;
	
	@OneToMany(mappedBy="category", fetch=FetchType.EAGER)
    @JsonManagedReference
	private List<Topic> topics;

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
