package br.edu.ifmt.cba.ifmthub.model.compositekeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PostFavoriteId {
	@Column(name = "id_user")
	private Long idUser;
	@Column(name = "id_post")
	private Long idPost;
	
	public PostFavoriteId() {
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getIdPost() {
		return idPost;
	}

	public void setIdPost(Long idPost) {
		this.idPost = idPost;
	}
}
