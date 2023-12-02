package br.edu.ifmt.cba.ifmthub.resources;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.model.dto.PostGridDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostInsertDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostResponseDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostResponseWithCommentsDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostTendencyDTO;
import br.edu.ifmt.cba.ifmthub.services.PostService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/post")
public class PostResource {

	@Autowired
	private PostService postService;

	@Transactional
	@PostMapping()
	public ResponseEntity save(@RequestBody @Valid PostInsertDTO postInsertDTO) {
		Post postSaved = this.postService.save(postInsertDTO);
		PostResponseDTO postResponse = new PostResponseDTO(postSaved, postSaved.getPhoto());
		return new ResponseEntity<PostResponseDTO>(postResponse, HttpStatus.CREATED);
	}
	
	@Transactional
	@PostMapping(consumes = { "multipart/form-data" }, value = "/{idPost}")
	public ResponseEntity saveImageInPost(@RequestParam(value = "file", required = true) MultipartFile file,
			@PathVariable Long idPost) {
		Post postSaved = this.postService.saveImageInPost(file, idPost);
		PostResponseDTO postResponse = new PostResponseDTO(postSaved, postSaved.getPhoto());
		return new ResponseEntity<PostResponseDTO>(postResponse, HttpStatus.CREATED);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<PostResponseDTO>> findAllFilteredByQueryText(@RequestParam String query) {
		List<PostResponseDTO> postList = this.postService.findAllFilteredByQueryText(query);
		return new ResponseEntity<List<PostResponseDTO>>(postList, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<PostResponseDTO>> findAll() {
		List<PostResponseDTO> postList = this.postService.findAll();
		return new ResponseEntity<List<PostResponseDTO>>(postList, HttpStatus.OK);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<PostGridDTO>> findAllByLoggedInUser() {
		List<PostGridDTO> postList = this.postService.findAllByLoggedInUser();
		return new ResponseEntity<List<PostGridDTO>>(postList, HttpStatus.OK);
	}

	@GetMapping("/bookmark")
	public ResponseEntity<List<PostResponseDTO>> findAllBookmark() {
		List<PostResponseDTO> postList = this.postService.findAllBookmark();
		return new ResponseEntity<List<PostResponseDTO>>(postList, HttpStatus.OK);
	}
	
	@GetMapping("/tendency")
	public ResponseEntity<List<PostTendencyDTO>> findTendencyPosts() {
		List<PostTendencyDTO> postList = this.postService.findTendencyPosts();
		return new ResponseEntity<List<PostTendencyDTO>>(postList, HttpStatus.OK);
	}

	@GetMapping("/{idPost}")
	public ResponseEntity<PostResponseWithCommentsDTO> findById(@PathVariable Long idPost) {
		PostResponseWithCommentsDTO postFound = this.postService.findByIdWithComments(idPost);
		return new ResponseEntity<PostResponseWithCommentsDTO>(postFound, HttpStatus.OK);
	}

	@PostMapping("/bookmark/{idPost}")
	public ResponseEntity<Map<String, Object>> toggleBookmark(@PathVariable Long idPost) {
		String response = this.postService.toggleBookmark(idPost);
		return new ResponseEntity<Map<String, Object>>(Map.of("message", response), HttpStatus.CREATED);
	}

	@PostMapping("/favorite/{idPost}")
	public ResponseEntity<Map<String, Object>> toggleFavorite(@PathVariable Long idPost) {
		String response = this.postService.toggleFavorite(idPost);
		return new ResponseEntity<Map<String, Object>>(Map.of("message", response), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{idPost}")
	public ResponseEntity<Void> deleteByIdPost(@PathVariable Long idPost) {
		this.postService.deleteByIdPost(idPost);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
