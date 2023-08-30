package br.edu.ifmt.cba.ifmthub.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifmt.cba.ifmthub.model.Course;
import br.edu.ifmt.cba.ifmthub.services.CourseService;

@RestController
@RequestMapping(value = "/course")
public class CourseResource {

	@Autowired
	private CourseService courseService;
	
	@PostMapping
	public ResponseEntity<Course> save(@RequestBody Course course) {
		Course courseSaved = this.courseService.save(course);
		return new ResponseEntity<Course>(courseSaved, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Course>> findAll() {
		List<Course> courseList = this.courseService.findAll();
		return new ResponseEntity<List<Course>>(courseList, HttpStatus.OK);
	}
	
	@GetMapping("/{idCourse}")
	public ResponseEntity<Course> findById(@PathVariable Long idCourse) {
		Course courseFound = this.courseService.findById(idCourse);
		return new ResponseEntity<Course>(courseFound, HttpStatus.OK);
	}
}
