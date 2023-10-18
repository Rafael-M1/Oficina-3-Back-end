package br.edu.ifmt.cba.ifmthub.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;
	
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
	@Column(length = 1)
	private String gender;
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	@Column(name = "birth_date")
	private LocalDate birthDate;
	@Column(name = "url_img_profile")
	private String urlImgProfile;
	private boolean status;
	private boolean isAccountConfirmed;
	private String password;
	private String email;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_user_role",
				joinColumns = @JoinColumn(name = "id_user"),
				inverseJoinColumns = @JoinColumn(name = "id_role"))
	private Set<Role> roles = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_bookmark",
				joinColumns = @JoinColumn(name = "id_user"),
				inverseJoinColumns = @JoinColumn(name = "id_post"))
	private Set<Post> bookmarks = new HashSet<>();
	
	@Lob
	@Column(name = "photo", columnDefinition = "BLOB")
	private byte[] photo;
	
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

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void addRole(Role... roleArgument) {
		for (Role roleObj : roleArgument) {
			this.roles.add(roleObj);			
		}
	}
	
	public Set<Post> getBookmark() {
		return bookmarks;
	}

	public void addBookMark(Post... postArgument) {
		for (Post postObj : postArgument) {
			this.bookmarks.add(postObj);			
		}
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAccountConfirmed() {
		return isAccountConfirmed;
	}

	public void setAccountConfirmed(boolean isAccountConfirmed) {
		this.isAccountConfirmed = isAccountConfirmed;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
