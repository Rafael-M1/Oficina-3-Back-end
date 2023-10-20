package br.edu.ifmt.cba.ifmthub.resources;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifmt.cba.ifmthub.model.Post;
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

	@Transactional
	@PostMapping(consumes = { "multipart/form-data" })
	public ResponseEntity save(@RequestParam(value = "data") @Valid String postInsertDTO,
			@RequestParam(value = "file", required = true) MultipartFile file) {
		ObjectMapper objectMapper = new ObjectMapper();
		PostInsertDTO postInsertDTOObject;
		try {
			postInsertDTOObject = objectMapper.readValue(postInsertDTO, PostInsertDTO.class);
		} catch (JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		Post postSaved = this.postService.save(postInsertDTOObject, file);
		PostResponseDTO postResponse = new PostResponseDTO(postSaved, postSaved.getPhoto());
		return new ResponseEntity<PostResponseDTO>(postResponse, HttpStatus.CREATED);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<PostResponseDTO>> findAllFilteredByQueryText(@RequestParam String query) {
		List<PostResponseDTO> postList = this.postService.findAllFilteredByQueryText(query);
		return new ResponseEntity<List<PostResponseDTO>>(postList, HttpStatus.OK);
	}

	// TODO return page instead
	@GetMapping
	public ResponseEntity<List<PostResponseDTO>> findAll() {
		List<PostResponseDTO> postList = this.postService.findAll();
		return new ResponseEntity<List<PostResponseDTO>>(postList, HttpStatus.OK);
	}

	// TODO return page instead
	@GetMapping("/bookmark")
	public ResponseEntity<List<PostResponseDTO>> findAllBookmark() {
		List<PostResponseDTO> postList = this.postService.findAllBookmark();
		return new ResponseEntity<List<PostResponseDTO>>(postList, HttpStatus.OK);
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
}
