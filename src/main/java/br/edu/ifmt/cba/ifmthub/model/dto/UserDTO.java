package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.edu.ifmt.cba.ifmthub.model.User;

public class UserDTO {
	private Long idUser;
	private String email;
	private String fullName;
	private String gender;
	private LocalDateTime dateCreated;
	private LocalDate birthDate;
	private byte[] photo;
	
	public UserDTO() {
	}

	public UserDTO(User user) {
		this.idUser = user.getIdUser();
		this.email = user.getEmail();
		this.fullName = user.getFullName();
		this.gender = user.getGender();
		this.dateCreated = user.getDateCreated();
		this.birthDate = user.getBirthDate();
		this.photo = user.getPhoto();
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "UserDTO [idUser=" + idUser + ", email=" + email + ", fullName=" + fullName + ", gender=" + gender
				+ ", dateCreated=" + dateCreated + ", birthDate=" + birthDate + "]";
	}
}
