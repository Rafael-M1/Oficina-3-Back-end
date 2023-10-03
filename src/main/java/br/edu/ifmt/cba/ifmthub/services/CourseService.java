package br.edu.ifmt.cba.ifmthub.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifmt.cba.ifmthub.model.Course;
import br.edu.ifmt.cba.ifmthub.repositories.CourseRepository;
import br.edu.ifmt.cba.ifmthub.resources.exceptions.ResourceNotFoundException;

@Service
public class CourseService {
	@Autowired
	private CourseRepository courseRepository;

	public Course save(Course course) {
		course.setDateCreated(LocalDateTime.now());
		course.setStatus(true);
		return courseRepository.save(course);
	}

	public Course findById(Long idCourse) {
		return courseRepository.findById(idCourse).orElseThrow(
				() -> new ResourceNotFoundException("No course present with idCourse = " + idCourse));
	}

	public List<Course> findAll() {
		return courseRepository.findAll();
	}

	public Course update(Course course) {
		Course courseSaved = this.findById(course.getIdCourse());
		courseSaved.setDescription(course.getDescription());
		courseSaved.setStatus(course.isStatus());
		courseRepository.save(courseSaved);
		return courseSaved;
	}

	public void delete(Long idCourseId) {
		courseRepository.deleteById(idCourseId);
	}
}
