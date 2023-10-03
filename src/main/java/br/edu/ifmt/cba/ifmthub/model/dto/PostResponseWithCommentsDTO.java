package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.edu.ifmt.cba.ifmthub.model.Post;

public class PostResponseWithCommentsDTO {
	private Long idPost;
	private UserDTO author;
	private CategoryDTO category;
	private Set<TagDTO> tags = new HashSet<>();
	private List<CommentDTO> comments = new ArrayList<>();
	private String title;
	private String subtitle;
	private String content;
	private LocalDateTime dateCreated;
	private String urlImgPost;
	private boolean status;
	
	public PostResponseWithCommentsDTO() {
	}

	public PostResponseWithCommentsDTO(Post post) {
		this.idPost = post.getIdPost();
		this.author = new UserDTO(post.getAuthor());
		this.category = new CategoryDTO(post.getCategory());
		this.tags = post.getTags().stream().map(tag -> new TagDTO(tag)).collect(Collectors.toSet());
		this.comments = post.getComments().stream().map(comment -> new CommentDTO(comment)).collect(Collectors.toList());
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

	public List<CommentDTO> getComments() {
		return comments;
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

	@Override
	public String toString() {
		return "PostResponseWithCommentsDTO [idPost=" + idPost + ", author=" + author + ", category=" + category
				+ ", tags=" + tags + ", comments=" + comments + ", title=" + title + ", subtitle=" + subtitle
				+ ", content=" + content + ", dateCreated=" + dateCreated + ", urlImgPost=" + urlImgPost + ", status="
				+ status + "]";
	}
}
