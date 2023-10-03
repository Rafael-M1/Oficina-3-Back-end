package br.edu.ifmt.cba.ifmthub.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentInsertDTO {
	@NotNull(message = "Must not be null")
	private Long idPost;
	@Size(min = 10, message = "Minimum length: 10")
	@NotNull(message = "Must not be null")
	private String content;
	@NotNull(message = "Must not be null")
	private boolean status;

	public CommentInsertDTO() {
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

	@Override
	public String toString() {
		return "CommentInsertDTO [idPost=" + idPost + ", content=" + content + ", status=" + status + "]";
	}
}
