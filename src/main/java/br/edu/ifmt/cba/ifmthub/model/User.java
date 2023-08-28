package br.edu.ifmt.cba.ifmthub.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	private Long idUser;
	@ManyToMany
	@JoinTable(
			uniqueConstraints = @UniqueConstraint(
					columnNames = {"id_user", "id_course"}),
			name = "user_course",
			joinColumns = @JoinColumn(name = "id_user"),
			inverseJoinColumns = @JoinColumn(name = "id_course"))
	private Set<Course> courses = new HashSet<>();
	@Column(name = "full_name")
	private String fullName;
	private char gender;
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	@Column(name = "url_img_profile")
	private String urlImgProfile;
	private boolean status;
	//TODO attribute admin
	
	public User() {
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getUrlImgProfile() {
		return urlImgProfile;
	}

	public void setUrlImgProfile(String urlImgProfile) {
		this.urlImgProfile = urlImgProfile;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course... coursesArgument) {
		for (Course courseObj : coursesArgument) {
			this.courses.add(courseObj);			
		}
	}
}
