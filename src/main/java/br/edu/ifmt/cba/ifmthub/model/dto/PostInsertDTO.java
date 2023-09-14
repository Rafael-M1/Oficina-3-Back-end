package br.edu.ifmt.cba.ifmthub.model.dto;

import java.util.ArrayList;
import java.util.List;

public class PostInsertDTO {
	private Long idAuthor;
	private CategoryDTO category;
	private List<TagDTO> tags = new ArrayList<>();
	private String title;
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
}