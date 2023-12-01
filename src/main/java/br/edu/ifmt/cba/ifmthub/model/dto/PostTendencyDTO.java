package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDateTime;

import br.edu.ifmt.cba.ifmthub.model.Post;

public class PostTendencyDTO {
	private UserDTO author;
	private Long idPost;
	private String title;
	private LocalDateTime dateCreated;

	public PostTendencyDTO() {
	}

	public PostTendencyDTO(Post post) {
		this.author = post.getAuthor() != null ? new UserDTO(post.getAuthor()) : null;
		this.idPost = post.getIdPost();
		this.title = post.getTitle();
		this.dateCreated = post.getDateCreated();
	}

	public UserDTO getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public Long getIdPost() {
		return idPost;
	}
}