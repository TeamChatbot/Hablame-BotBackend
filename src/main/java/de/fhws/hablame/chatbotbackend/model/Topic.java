package de.fhws.hablame.chatbotbackend.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Model class for a topic. Which is logically under the category and above the content.
 * A topic has one category as parent and one or more contents as childs.
 */
@Entity
@Table(name="topic")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Topic extends MappedSuperClass {
	
	//TODO: rethink the association to category and content, maybe n:m relations?

	@Transient
	private static final long serialVersionUID = -4860147072470751L;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="category_id")
	@JsonBackReference
	private Category category;

	@OneToMany(mappedBy="topic", fetch=FetchType.EAGER)
    @JsonManagedReference
	private List<Content> contents;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
