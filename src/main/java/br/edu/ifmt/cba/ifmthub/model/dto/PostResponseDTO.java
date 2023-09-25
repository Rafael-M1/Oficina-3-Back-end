package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import br.edu.ifmt.cba.ifmthub.model.Post;

public class PostResponseDTO {
	private Long idPost;
	private UserDTO author;
	private CategoryDTO category;
	private Set<TagDTO> tags = new HashSet<>();
	private String title;
	private String subtitle;
	private String content;
	private LocalDateTime dateCreated;
	private String urlImgPost;
	private boolean status;

	public PostResponseDTO() {
	}

	public PostResponseDTO(Post post) {
		this.idPost = post.getIdPost();
		this.author = new UserDTO(post.getAuthor());
		this.category = new CategoryDTO(post.getCategory());
		this.tags = post.getTags().stream().map(tag -> new TagDTO(tag)).collect(Collectors.toSet());
		this.title = post.getTitle();
		this.subtitle = post.getSubTitle();
		this.content = post.getContent();
		this.dateCreated = post.getDateCreated();
		this.urlImgPost = post.getUrlImgPost();
		this.status = post.isStatus();
	}

	public Long getIdPost() {
		return idPost;
	}

	public UserDTO getAuthor() {
		return author;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public Set<TagDTO> getTags() {
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

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public String getUrlImgPost() {
		return urlImgPost;
	}

	public boolean isStatus() {
		return status;
	}
}