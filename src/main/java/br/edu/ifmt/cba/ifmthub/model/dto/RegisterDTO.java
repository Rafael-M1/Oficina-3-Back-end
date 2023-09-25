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
	@Email(message = "Invalid e-mail")
	@NotNull(message = "Must not be null")
	private String email;
	@Size(min = 6, message = "Minimum length: 6")
	@NotNull(message = "Must not be null")
	private String password;
	@NotBlank(message = "Must not be blank")
	private String fullName;
	@Pattern(message = "Values allowed: ['M', 'F']", regexp = "\\b(M|F)\\b")
	@NotNull(message = "Must not be null")
	private String gender;
	@Past
	@NotNull(message = "Must not be null")
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

	public String getGender() {
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
