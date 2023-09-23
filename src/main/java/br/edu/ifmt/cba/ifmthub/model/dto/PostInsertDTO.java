package br.edu.ifmt.cba.ifmthub.model.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PostInsertDTO {
	@NotNull(message = "Author ID must not be empty")
	private Long idAuthor;
	@NotNull(message = "Category must not be empty")
	@Valid
	private CategoryDTO category;
	@NotEmpty(message = "Tag must not be empty")
	@NotNull
	@Valid
	private List<TagDTO> tags = new ArrayList<>();
	@NotBlank(message = "Title must not be empty")
	private String title;
	@NotBlank(message = "Content must not be empty")
	private String content;
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

	@Override
	public String toString() {
		return "PostInsertDTO [idAuthor=" + idAuthor + ", category=" + category + ", tags=" + tags + ", title=" + title
				+ ", content=" + content + ", urlImgPost=" + urlImgPost + "]";
	}
}