package br.edu.ifmt.cba.ifmthub.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_view")
public class PostView {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_post_view")
	private Long idPostView;
	@ManyToOne
	@JoinColumn(name = "id_post", foreignKey = @ForeignKey(name = "fk_post_view_post"))
	private Post post;
	@ManyToOne
	@JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_post_view_user"), nullable = true)
	private User viewer;
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	
	public PostView() {
	}

	public Long getIdPostView() {
		return idPostView;
	}

	public void setIdPostView(Long idPostView) {
		this.idPostView = idPostView;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getViewer() {
		return viewer;
	}

	public void setViewer(User viewer) {
		this.viewer = viewer;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}
}
