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

import br.edu.ifmt.cba.ifmthub.model.Category;
import br.edu.ifmt.cba.ifmthub.services.CategoryService;

@RestController
@RequestMapping(value = "/category")
public class CategoryResource {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping
	public ResponseEntity<Category> save(@RequestBody Category category) {
		Category categorySaved = this.categoryService.save(category);
		return new ResponseEntity<Category>(categorySaved, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		List<Category> categoryList = this.categoryService.findAll();
		return new ResponseEntity<List<Category>>(categoryList, HttpStatus.OK);
	}
	
	@GetMapping("/{idCategory}")
	public ResponseEntity<Category> findById(@PathVariable Long idCategory) {
		Category categoryFound = this.categoryService.findById(idCategory);
		return new ResponseEntity<Category>(categoryFound, HttpStatus.OK);
	}
}
