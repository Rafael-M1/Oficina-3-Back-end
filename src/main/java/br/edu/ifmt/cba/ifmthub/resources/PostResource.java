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

import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.services.PostService;

@RestController
@RequestMapping(value = "/post")
public class PostResource {

	@Autowired
	private PostService postService;
	
	@PostMapping
	public ResponseEntity<Post> save(@RequestBody Post post) {
		Post postSaved = this.postService.save(post);
		return new ResponseEntity<Post>(postSaved, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Post>> findAll() {
		List<Post> postList = this.postService.findAll();
		return new ResponseEntity<List<Post>>(postList, HttpStatus.OK);
	}
	
	@GetMapping("/{idPost}")
	public ResponseEntity<Post> findById(@PathVariable Long idPost) {
		Post postFound = this.postService.findById(idPost);
		return new ResponseEntity<Post>(postFound, HttpStatus.OK);
	}
}
