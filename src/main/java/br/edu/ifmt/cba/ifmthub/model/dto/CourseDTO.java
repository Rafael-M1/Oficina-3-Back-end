package br.edu.ifmt.cba.ifmthub.model.dto;

import br.edu.ifmt.cba.ifmthub.model.Course;

public class CourseDTO {
	private Long idCourse;
	private String description;

	public CourseDTO() {
	}

	public CourseDTO(Course course) {
		this.idCourse = course.getIdCourse();
		this.description = course.getDescription();
	}

	public Long getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
