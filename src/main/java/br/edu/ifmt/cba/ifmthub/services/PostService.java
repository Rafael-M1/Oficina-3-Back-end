package br.edu.ifmt.cba.ifmthub.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.repositories.PostRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;

	public Post save(Post post) {
		post.setDateCreated(LocalDateTime.now());
		post.setStatus(true);
		return postRepository.save(post);
	}

	public Post findById(Long idPost) {
		return postRepository.findById(idPost).get();
	}

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	public Post update(Post post) {
		Post postSaved = postRepository.findById(post.getIdPost()).get();
		postSaved.setAuthor(post.getAuthor());
		postSaved.setCategory(post.getCategory());
		postSaved.setTitle(post.getTitle());
		postSaved.setContent(post.getContent());
		postSaved.setUrlImgPost(post.getUrlImgPost());
		postSaved.setStatus(post.isStatus());
		postRepository.save(postSaved);
		return postSaved;
	}

	public void delete(Long idPost) {
		postRepository.deleteById(idPost);
	}
}
