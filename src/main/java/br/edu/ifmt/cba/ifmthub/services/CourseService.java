package br.edu.ifmt.cba.ifmthub.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifmt.cba.ifmthub.model.Course;
import br.edu.ifmt.cba.ifmthub.repositories.CourseRepository;

@Service
public class CourseService {
	@Autowired
	private CourseRepository courseRepository;

	public Course save(Course course) {
		course.setDateCreated(LocalDateTime.now());
		course.setStatus(true);
		return courseRepository.save(course);
	}

	public Course findById(Long idCourseId) {
		return courseRepository.findById(idCourseId).get();
	}

	public List<Course> findAll() {
		return courseRepository.findAll();
	}

	public Course update(Course course) {
		Course courseSaved = courseRepository.findById(course.getIdCourse()).get();
		courseSaved.setDescription(course.getDescription());
		courseSaved.setStatus(course.isStatus());
		courseRepository.save(courseSaved);
		return courseSaved;
	}

	public void delete(Long idCourseId) {
		courseRepository.deleteById(idCourseId);
	}
}
