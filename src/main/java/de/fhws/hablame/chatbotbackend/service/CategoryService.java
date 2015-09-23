package de.fhws.hablame.chatbotbackend.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fhws.hablame.chatbotbackend.dto.CategoryDTO;
import de.fhws.hablame.chatbotbackend.model.Category;
import de.fhws.hablame.chatbotbackend.repository.CategoryRepository;

/**
 * Service class for the {@link Category} and the {@link CategoryRepository}.
 * Here we handle all logic which is used to get the {@link Category} data from the database.
 */
@Service
public class CategoryService implements IService<CategoryDTO, Category> {
	
	private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	/**
	 * Method to get all {@link Category}.
	 * @return {@link List} with all existent {@link Category}
	 */
	public List<Category> getAll() {
		return categoryRepository.findAll();
	}
	
	/**
	 * Method to get a specific {@link Category} by its id.
	 * @param id
	 * @return found {@link Category} or null
	 */
	public Category getById(Long id) {
		return categoryRepository.findOne(id);
	}
	
	/**
	 * Method to get a specific {@link Category} by its name.
	 * @param categoryName
	 * @return found {@link Category} or null
	 */
	public Category getByName(String categoryName) {
		return categoryRepository.findByName(categoryName);
	}
	
	/**
	 * Method to get the number of {@link Category}.
	 * @return
	 */
	public long count() {
		return categoryRepository.count();
	}
	
	/**
	 * Method to create a {@link Category} with the given {@link CategoryDTO}.
	 * @param categoryDTO
	 * @return the created {@link Category} or null
	 */
	public Category create(CategoryDTO categoryDTO) {
		Category category = null;
		if (categoryDTO != null) {
			if (categoryRepository.findByName(categoryDTO.getName()) == null) {
				category = new Category();
				category.setActive(categoryDTO.isActive());
				category.setCreateTime(new Date());
				category.setName(categoryDTO.getName());
				category = categoryRepository.save(category);
				LOG.info("Created category with name {}", category.getName());
			} else {
				LOG.warn("Could not create category with already existent name {}", categoryDTO.getName());
			}
		} else {
			LOG.warn("Could not create category without DTO");
		}
		return category;
	}
	
	/**
	 * Method to delete a {@link Category} by its name.
	 * @param categoryName
	 */
	public void deleteCategoryByName(String categoryName) {
		if (categoryName != null) {
			categoryRepository.delete(categoryRepository.findByName(categoryName));
			LOG.info("Deleted category with name {}", categoryName);
		} else {
			LOG.warn("Could not delete category without name");
		}
	}
	
	/**
	 * Method to delete a {@link Category} by its id.
	 * @param id
	 * @return true if Category could be deleted and false otherwise
	 */
	public Boolean deleteById(Long id) {
		if (id != null) {
			if (categoryRepository.findOne(id) != null) {
				LOG.info("Deleted Category with id {}", id);
				return true;
			} else {
				LOG.warn("Could not delete Category with invalid id {}", id);
			}
		} else {
			LOG.warn("Could not delete Category without id");
		}
		return false;
	}
}
