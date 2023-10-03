package br.edu.ifmt.cba.ifmthub.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifmt.cba.ifmthub.model.dto.CommentDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.CommentInsertDTO;
import br.edu.ifmt.cba.ifmthub.services.CommentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/comment")
public class CommentResource {

	@Autowired
	private CommentService commentService;

	// TODO enable only to admin users
	@GetMapping
	public ResponseEntity<List<CommentDTO>> listAll() {
		List<CommentDTO> listCommentDTO = commentService.listAllComments();
		return new ResponseEntity<List<CommentDTO>>(listCommentDTO, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<CommentDTO> save(@RequestBody @Valid CommentInsertDTO commentInsertDTO) {
		CommentDTO commentDTO = commentService.save(commentInsertDTO);
		return new ResponseEntity<CommentDTO>(commentDTO, HttpStatus.OK);
	}
}
