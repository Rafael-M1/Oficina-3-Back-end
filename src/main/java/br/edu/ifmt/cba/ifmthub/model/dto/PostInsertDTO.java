package br.edu.ifmt.cba.ifmthub.model.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PostInsertDTO {
	@NotNull(message = "Author ID must not be empty")
	private Long idAuthor;
	@NotNull
	private CategoryDTO category;
	@NotEmpty
	private List<TagDTO> tags = new ArrayList<>();
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	@NotBlank
	private String urlImgPost;

	public PostInsertDTO() {
	}

	public Long getIdAuthor() {
		return idAuthor;
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

	public String getContent() {
		return content;
	}

	public String getUrlImgPost() {
		return urlImgPost;
	}
}