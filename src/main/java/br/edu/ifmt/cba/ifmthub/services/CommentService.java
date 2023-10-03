package br.edu.ifmt.cba.ifmthub.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifmt.cba.ifmthub.model.Comment;
import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.model.dto.CommentDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.CommentInsertDTO;
import br.edu.ifmt.cba.ifmthub.repositories.CommentRepository;
import br.edu.ifmt.cba.ifmthub.repositories.PostRepository;
import br.edu.ifmt.cba.ifmthub.resources.exceptions.ResourceNotFoundException;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Transactional
	public CommentDTO save(CommentInsertDTO commentInsertDTO) {
		Comment comment = new Comment();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User commenter = (User) authentication.getPrincipal();
		if (authentication == null || commenter == null) {
			throw new ResourceNotFoundException("User not authenticated.");
		}
		comment.setCommenter(commenter);
		comment.setContent(commentInsertDTO.getContent());
		comment.setDateCreated(LocalDateTime.now());
		Post postFound = postRepository.findById(commentInsertDTO.getIdPost()).orElseThrow(
				() -> new ResourceNotFoundException("No post present with idPost = " + commentInsertDTO.getIdPost()));
		comment.setPost(postFound);
		comment.setStatus(true);

		comment = commentRepository.save(comment);
		CommentDTO commentDTO = new CommentDTO(comment);
		return commentDTO;
	}

	public List<CommentDTO> listAllComments() {
		return commentRepository.findAll().stream().map(comment -> new CommentDTO(comment))
				.collect(Collectors.toList());
	}
}
