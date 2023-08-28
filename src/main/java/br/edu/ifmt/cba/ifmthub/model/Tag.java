package br.edu.ifmt.cba.ifmthub.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tag")
	private Long idTag;
	private String description;
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	private boolean status;
	// TODO atributo id_user
	
	@ManyToMany(mappedBy = "tags")
	private Set<Post> posts = new HashSet<>();
	
	public Tag() {
	}
	
	public Tag(Long idTag, String description, LocalDateTime dateCreated, boolean status) {
		this.idTag = idTag;
		this.description = description;
		this.dateCreated = dateCreated;
		this.status = status;
	}

	public Long getIdTag() {
		return idTag;
	}

	public void setIdTag(Long idTag) {
		this.idTag = idTag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
}
