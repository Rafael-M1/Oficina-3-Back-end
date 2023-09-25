package br.edu.ifmt.cba.ifmthub.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifmt.cba.ifmthub.model.Category;
import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.model.Tag;
import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.model.dto.PostInsertDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostResponseDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.TagDTO;
import br.edu.ifmt.cba.ifmthub.repositories.CategoryRepository;
import br.edu.ifmt.cba.ifmthub.repositories.PostRepository;
import br.edu.ifmt.cba.ifmthub.repositories.TagRepository;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TagRepository tagRepository;

	public Post save(Post post) {
		post.setDateCreated(LocalDateTime.now());
		post.setStatus(true);
		return postRepository.save(post);
	}

	public Post save(PostInsertDTO postInsertDTO) {
		Post post = new Post();
		User author = userRepository.findById(postInsertDTO.getIdAuthor()).get();
		post.setAuthor(author);
		Optional<Category> categoryOpt = categoryRepository
				.findByDescription(postInsertDTO.getCategory().getDescription());
		if (categoryOpt.isPresent()) {
			post.setCategory(categoryOpt.get());
		} else {
			Category category = new Category(null, postInsertDTO.getCategory().getDescription(), LocalDateTime.now(),
					true);
			category = categoryRepository.save(category);
			post.setCategory(category);
		}
		for (TagDTO tagDTO : postInsertDTO.getTags()) {
			Optional<Tag> tagOpt = tagRepository.findByDescription(tagDTO.getDescription());
			if (tagOpt.isPresent()) {
				post.addTag(tagOpt.get());
			} else {
				Tag tag = new Tag(null, tagDTO.getDescription(), LocalDateTime.now(), true);
				tag = tagRepository.save(tag);
				post.addTag(tag);
			}
		}
		post.setTitle(postInsertDTO.getTitle());
		post.setSubTitle(postInsertDTO.getSubtitle());
		post.setContent(postInsertDTO.getContent());
		post.setUrlImgPost(postInsertDTO.getUrlImgPost());
		post.setDateCreated(LocalDateTime.now());
		return postRepository.save(post);
	}

	public PostResponseDTO findById(Long idPost) {
		Post post = postRepository.findById(idPost).get();
		return new PostResponseDTO(post);
	}

	public List<PostResponseDTO> findAll() {
		return postRepository.findAll().stream().map(post -> new PostResponseDTO(post)).collect(Collectors.toList());
	}

	public Post update(Post post) {
		Post postSaved = postRepository.findById(post.getIdPost()).get();
		postSaved.setAuthor(post.getAuthor());
		postSaved.setCategory(post.getCategory());
		postSaved.setTitle(post.getTitle());
		postSaved.setSubTitle(post.getSubTitle());
		postSaved.setContent(post.getContent());
		postSaved.setUrlImgPost(post.getUrlImgPost());
		postSaved.setStatus(post.isStatus());
		postRepository.save(postSaved);
		return postSaved;
	}

	public void delete(Long idPost) {
		postRepository.deleteById(idPost);
	}

	public List<PostResponseDTO> findAllFilteredByQueryText(String query) {
		return postRepository.findAllFilteredByQueryText(query).stream().map(post -> new PostResponseDTO(post))
				.collect(Collectors.toList());
	}
}
