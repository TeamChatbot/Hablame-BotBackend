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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Model class for a topic. Which is logically under the category and above the content.
 * A topic has one category as parent and one or more contents as childs.
 */
@Entity
@Table(name="topic", uniqueConstraints={
		@UniqueConstraint(columnNames={"name", "category_id"}, name="name_and_category_id")})
@JsonIgnoreProperties(ignoreUnknown=true)
public class Topic extends MappedSuperClass{

	@Transient
	private static final long serialVersionUID = -4860147072470751L;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="category_id", nullable=false)
	@JsonBackReference
	private Category category;

	@OneToMany(mappedBy="topic", fetch=FetchType.EAGER)
    @JsonManagedReference
	private List<Content> content;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Content> getContent() {
		return content;
	}

	public void setContent(List<Content> content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
