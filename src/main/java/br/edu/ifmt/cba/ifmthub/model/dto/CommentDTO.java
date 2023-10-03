package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDateTime;

import br.edu.ifmt.cba.ifmthub.model.Comment;

public class CommentDTO {
	private Long idComment;
	private UserDTO commenter;
	private Long idPost;
	private String content;
	private LocalDateTime dateCreated;
	private boolean status;
	
	public CommentDTO() {
	}

	public CommentDTO(Comment comment) {
		this.idComment = comment.getIdComment();
		this.commenter = new UserDTO(comment.getCommenter());
		this.idPost = comment.getPost().getIdPost();
		this.content = comment.getContent();
		this.dateCreated = comment.getDateCreated();
		this.status = comment.isStatus();
	}

	public Long getIdComment() {
		return idComment;
	}

	public UserDTO getCommenter() {
		return commenter;
	}

	public Long getIdPost() {
		return idPost;
	}

	public String getContent() {
		return content;
	}

	public boolean isStatus() {
		return status;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	@Override
	public String toString() {
		return "CommentDTO [idComment=" + idComment + ", commenter=" + commenter + ", idPost=" + idPost + ", content="
				+ content + ", dateCreated=" + dateCreated + ", status=" + status + "]";
	}
}
