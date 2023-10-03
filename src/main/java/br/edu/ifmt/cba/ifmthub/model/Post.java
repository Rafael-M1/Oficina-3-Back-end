package br.edu.ifmt.cba.ifmthub.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_post")
	private Long idPost;
	@ManyToOne
	@JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_post_user"))
	private User author;
	@ManyToOne
	@JoinColumn(name = "id_category", foreignKey = @ForeignKey(name = "fk_post_category"))
	private Category category;
	@ManyToMany
	@JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = { "id_post",
			"id_tag" }), name = "post_tag", joinColumns = @JoinColumn(name = "id_post"), inverseJoinColumns = @JoinColumn(name = "id_tag"))
	private Set<Tag> tags = new HashSet<>();
	private String title;
	@Column(name = "sub_title")
	private String subTitle;
	private String content;
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	@Column(name = "url_img_post")
	private String urlImgPost;
	private boolean status;
	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();
	
	@ManyToMany(mappedBy = "bookmarks")
	private Set<User> usersBookmarks = new HashSet<>();

	public Post() {
	}

	public Post(Long idPost, User author, Category category, String title, String subTitle, String content,
			LocalDateTime dateCreated, String urlImgPost, boolean status) {
		this.idPost = idPost;
		this.author = author;
		this.category = category;
		this.title = title;
		this.subTitle = subTitle;
		this.content = content;
		this.dateCreated = dateCreated;
		this.urlImgPost = urlImgPost;
		this.status = status;
	}

	public Long getIdPost() {
		return idPost;
	}

	public void setIdPost(Long idPost) {
		this.idPost = idPost;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void addTag(Tag... tagsArgument) {
		for (Tag tagObj : tagsArgument) {
			this.tags.add(tagObj);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getUrlImgPost() {
		return urlImgPost;
	}

	public void setUrlImgPost(String urlImgPost) {
		this.urlImgPost = urlImgPost;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
