package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import br.edu.ifmt.cba.ifmthub.model.Category;
import br.edu.ifmt.cba.ifmthub.model.Post;

public class PostGridDTO {
	private Long idPost;
	private String author;
	private Category category;
	private Set<TagDTO> tags = new HashSet<>();
	private String title;
	private LocalDateTime dateCreated;
	private Long countFavorites;
	private Long countBookmarks;

	public PostGridDTO() {
	}

	public PostGridDTO(Post post) {
		this.idPost = post.getIdPost();
		this.author = post.getAuthor().getFullName();
		this.category = post.getCategory();
		this.tags = post.getTags().stream().map(tag -> new TagDTO(tag)).collect(Collectors.toSet());
		this.title = post.getTitle();
		this.dateCreated = post.getDateCreated();
	}

	public Long getIdPost() {
		return idPost;
	}

	public String getAuthor() {
		return author;
	}

	public Category getCategory() {
		return category;
	}

	public Set<TagDTO> getTags() {
		return tags;
	}

	public String getTitle() {
		return title;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public Long getCountFavorites() {
		return countFavorites;
	}

	public Long getCountBookmarks() {
		return countBookmarks;
	}

	@Override
	public String toString() {
		return "PostGridDTO [idPost=" + idPost + ", author=" + author + ", category=" + category + ", tags=" + tags
				+ ", title=" + title + ", dateCreated=" + dateCreated + ", countFavorites=" + countFavorites
				+ ", countBookmarks=" + countBookmarks + "]";
	}
}
