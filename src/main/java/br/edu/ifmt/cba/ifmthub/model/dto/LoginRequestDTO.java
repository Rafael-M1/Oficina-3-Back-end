package br.edu.ifmt.cba.ifmthub.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {
	@Email(message = "Invalid e-mail")
	@NotNull(message = "Must not be null")
	private String email;
	@Size(min = 6, message = "Minimum length: 6")
	@NotNull(message = "Must not be null")
	private String password;

	public LoginRequestDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequestDTO [email=" + email + ", password=" + password + "]";
	}
}
