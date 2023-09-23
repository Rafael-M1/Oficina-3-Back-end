package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RegisterDTO {
	private String email;
	private String password;
	private String fullName;
	private char gender;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthDate;
	private String urlImgProfile;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getFullName() {
		return fullName;
	}

	public char getGender() {
		return gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getUrlImgProfile() {
		return urlImgProfile;
	}

	@Override
	public String toString() {
		return "RegisterDTO [email=" + email + ", password=" + password + ", fullName=" + fullName + ", gender="
				+ gender + ", birthDate=" + birthDate + ", urlImgProfile=" + urlImgProfile + "]";
	}
}
