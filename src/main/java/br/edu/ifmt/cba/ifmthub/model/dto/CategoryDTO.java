package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDateTime;

import br.edu.ifmt.cba.ifmthub.model.Category;
import jakarta.validation.constraints.NotEmpty;

public class CategoryDTO {
	private Long idCategory;
	@NotEmpty(message = "Category's description must not be empty")
	private String description;
	private LocalDateTime dateCreated;
	private boolean status;

	public CategoryDTO() {
	}

	public CategoryDTO(Category category) {
		this.idCategory = category.getIdCategory();
		this.description = category.getDescription();
		this.dateCreated = category.getDateCreated();
		this.status = category.isStatus();
	}

	public Long getIdCategory() {
		return idCategory;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public boolean isStatus() {
		return status;
	}
}
