package br.edu.ifmt.cba.ifmthub.model.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PostUpdateDTO {
	@NotNull(message = "Category must not be empty")
	@Valid
	private CategoryDTO category;
	@NotEmpty(message = "Tag must not be empty")
	@NotNull(message = "Tag must not be null")
	@Valid
	private List<TagDTO> tags = new ArrayList<>();
	@NotBlank(message = "Title must not be empty")
	private String title;
	@NotBlank(message = "Subtitle must not be empty")
	private String subtitle;
	@NotBlank(message = "Content must not be empty")
	private String content;

	public PostUpdateDTO() {
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public List<TagDTO> getTags() {
		return tags;
	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "PostUpdateDTO [category=" + category + ", tags=" + tags + ", title=" + title + ", subtitle=" + subtitle
				+ ", content=" + content + "]";
	}
}