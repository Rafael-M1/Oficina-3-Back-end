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

import br.edu.ifmt.cba.ifmthub.model.Tag;
import br.edu.ifmt.cba.ifmthub.services.TagService;

@RestController
@RequestMapping(value = "/tag")
public class TagResource {

	@Autowired
	private TagService tagService;
	
	@PostMapping
	public ResponseEntity<Tag> save(@RequestBody Tag tag) {
		Tag tagSaved = this.tagService.save(tag);
		return new ResponseEntity<Tag>(tagSaved, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Tag>> findAll() {
		List<Tag> tagList = this.tagService.findAll();
		return new ResponseEntity<List<Tag>>(tagList, HttpStatus.OK);
	}
	
	@GetMapping("/{idTag}")
	public ResponseEntity<Tag> findById(@PathVariable Long idTag) {
		Tag tagFound = this.tagService.findById(idTag);
		return new ResponseEntity<Tag>(tagFound, HttpStatus.OK);
	}
}
