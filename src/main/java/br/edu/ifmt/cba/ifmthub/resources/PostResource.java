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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifmt.cba.ifmthub.model.dto.PostInsertDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostResponseDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostResponseWithCommentsDTO;
import br.edu.ifmt.cba.ifmthub.services.PostService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/post")
public class PostResource {

	@Autowired
	private PostService postService;
	
	@PostMapping
	public ResponseEntity<PostResponseDTO> save(@RequestBody @Valid PostInsertDTO postInsertDTO) {
		PostResponseDTO postSaved = new PostResponseDTO(this.postService.save(postInsertDTO));
		return new ResponseEntity<PostResponseDTO>(postSaved, HttpStatus.CREATED);
	}
	
	@GetMapping("/filter")
	public ResponseEntity<List<PostResponseDTO>> findAllFilteredByQueryText(@RequestParam String query) {
		List<PostResponseDTO> postList = this.postService.findAllFilteredByQueryText(query);
		return new ResponseEntity<List<PostResponseDTO>>(postList, HttpStatus.OK);
	}
	
	//TODO return page instead
	@GetMapping
	public ResponseEntity<List<PostResponseDTO>> findAll() {
		List<PostResponseDTO> postList = this.postService.findAll();
		return new ResponseEntity<List<PostResponseDTO>>(postList, HttpStatus.OK);
	}
	
	@GetMapping("/{idPost}")
	public ResponseEntity<PostResponseWithCommentsDTO> findById(@PathVariable Long idPost) {
		PostResponseWithCommentsDTO postFound = this.postService.findByIdWithComments(idPost);
		return new ResponseEntity<PostResponseWithCommentsDTO>(postFound, HttpStatus.OK);
	}
}
