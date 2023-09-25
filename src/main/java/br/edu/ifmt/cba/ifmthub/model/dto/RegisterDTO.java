package br.edu.ifmt.cba.ifmthub.model.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {
	@Email
	@NotNull
	private String email;
	@Size(min = 6)
	@NotNull
	private String password;
	@NotBlank
	private String fullName;
	@Pattern(message = "Values allowed: ['M', 'F']", regexp = "\b(M|F)\b")
	@NotNull
	private char gender;
	@Past
	@NotNull
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
