package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDateTime;

import br.edu.ifmt.cba.ifmthub.model.Tag;
import jakarta.validation.constraints.NotEmpty;

public class TagDTO {
	private Long idTag;
	@NotEmpty(message = "Tag's description must not be empty")
	private String description;
	private LocalDateTime dateCreated;
	private boolean status;

	public TagDTO() {
	}

	public TagDTO(Tag tag) {
		this.idTag = tag.getIdTag();
		this.description = tag.getDescription();
		this.dateCreated = tag.getDateCreated();
		this.status = tag.isStatus();
	}

	public Long getIdTag() {
		return idTag;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public boolean isStatus() {
		return status;
	}
}
