package br.edu.ifmt.cba.ifmthub.model;

import java.time.LocalDateTime;

import br.edu.ifmt.cba.ifmthub.model.compositekeys.PostFavoriteId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_favorite")
public class PostFavorite {
	@EmbeddedId
	private PostFavoriteId idPostFavorite;
	@ManyToOne
	@MapsId("idUser")
	@JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_post_favorite_user"))
	private User user;
	@ManyToOne
	@MapsId("idPost")
	@JoinColumn(name = "id_post", foreignKey = @ForeignKey(name = "fk_post_favorite_post"))
	private Post post;
	@Column(name = "date_created")
	private LocalDateTime dateCreated;

	public PostFavorite() {
	}

	public PostFavoriteId getIdPostFavorite() {
		return idPostFavorite;
	}

	public void setIdPostFavorite(PostFavoriteId idPostFavorite) {
		this.idPostFavorite = idPostFavorite;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return "PostFavorite [idPostFavorite=" + idPostFavorite + ", user=" + user + ", post=" + post + ", dateCreated="
				+ dateCreated + "]";
	}
}
