package br.edu.ifmt.cba.ifmthub.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifmt.cba.ifmthub.model.Category;
import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.model.PostView;
import br.edu.ifmt.cba.ifmthub.model.Role;
import br.edu.ifmt.cba.ifmthub.model.Tag;
import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.model.dto.PostGridDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostInsertDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostResponseDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostResponseWithCommentsDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.PostTendencyDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.TagDTO;
import br.edu.ifmt.cba.ifmthub.repositories.CategoryRepository;
import br.edu.ifmt.cba.ifmthub.repositories.CommentRepository;
import br.edu.ifmt.cba.ifmthub.repositories.PostFavoriteRepository;
import br.edu.ifmt.cba.ifmthub.repositories.PostRepository;
import br.edu.ifmt.cba.ifmthub.repositories.PostViewRepository;
import br.edu.ifmt.cba.ifmthub.repositories.TagRepository;
import br.edu.ifmt.cba.ifmthub.resources.exceptions.ResourceNotFoundException;
import br.edu.ifmt.cba.ifmthub.utils.LoggedInUserUtils;

@Service
public class PostService {
	
	private static Logger logger = LogManager.getLogger(PostService.class);
	
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private PostViewRepository postViewRepository;
	
	@Autowired
	private PostFavoriteRepository postFavoriteRepository;
	
	@Autowired
	private CommentRepository commentRepository;

	public Post save(Post post) {
		post.setDateCreated(LocalDateTime.now());
		post.setStatus(true);
		return postRepository.save(post);
	}
	
	public Post saveImageInPost(MultipartFile file, Long idPost) {
		if (file.getSize() > 1048576) {
			throw new IllegalArgumentException("File exceeds its maximum permitted of 1048576 bytes.");
		}
		Post post = postRepository.findById(idPost)
				.orElseThrow(() -> new ResourceNotFoundException("No post present with idPost = " + idPost));
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = (User) authentication.getPrincipal();
		if (authentication == null || loggedInUser == null) {
			throw new ResourceNotFoundException("User not authenticated.");
		}
		if (!post.getAuthor().getIdUser().equals(loggedInUser.getIdUser())) {
			throw new ResourceNotFoundException("Current user cannot change this post.");
		}
		try {
			post.setPhoto(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return postRepository.save(post);
	}

	@Transactional
	public Post save(PostInsertDTO postInsertDTO) {
		Post post = new Post();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User author = (User) authentication.getPrincipal();
		if (authentication == null || author == null) {
			throw new ResourceNotFoundException("User not authenticated.");
		}
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
		post.setStatus(postInsertDTO.isStatus());
		post = postRepository.save(post);
		logger.info("Created post of id=" + post.getIdPost() + " by User of idUser=" + post.getAuthor().getIdUser());
		return post;
	}

	public PostResponseDTO findById(Long idPost) {
		Post post = postRepository.findById(idPost)
				.orElseThrow(() -> new ResourceNotFoundException("No post present with idPost = " + idPost));
		return new PostResponseDTO(post);
	}

	public List<PostResponseDTO> findAll() {
		final User loggedInUser = getLoggedInUser();
		return postRepository.findAll().stream().map(post -> {
			Long isFavorited = loggedInUser != null
					? postRepository.findFavorite(loggedInUser.getIdUser(), post.getIdPost())
					: 0l;
			Long isBookmarked = loggedInUser != null
					? postRepository.findBookmark(loggedInUser.getIdUser(), post.getIdPost())
					: 0l;
			Long countFavorites = postRepository.countFavorites(post.getIdPost());
			Long countBookmarks = postRepository.countBookmarks(post.getIdPost());
			return new PostResponseDTO(post, isBookmarked == 1 ? true : false, isFavorited == 1 ? true : false,
					countFavorites, countBookmarks, post.getPhoto());
		}).collect(Collectors.toList());
	}

	public Post update(Long idPost, Post post) {
		Post postSaved = postRepository.findById(post.getIdPost())
				.orElseThrow(() -> new ResourceNotFoundException("No post present with idPost = " + post.getIdPost()));
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
		final User loggedInUser = getLoggedInUser();
		return postRepository.findAllFilteredByQueryText(query).stream().map(post -> {
			Long isFavorited = loggedInUser != null
					? postRepository.findFavorite(loggedInUser.getIdUser(), post.getIdPost())
					: 0l;
			Long isBookmarked = loggedInUser != null
					? postRepository.findBookmark(loggedInUser.getIdUser(), post.getIdPost())
					: 0l;
			Long countFavorites = postRepository.countFavorites(post.getIdPost());
			Long countBookmarks = postRepository.countBookmarks(post.getIdPost());
			return new PostResponseDTO(post, isBookmarked == 1 ? true : false, isFavorited == 1 ? true : false,
					countFavorites, countBookmarks, post.getPhoto());
		}).collect(Collectors.toList());
	}

	public PostResponseWithCommentsDTO findByIdWithComments(Long idPost) {
		Long isFavorited = 0l;
		Long isBookmarked = 0l;
		Post post = postRepository.findById(idPost)
				.orElseThrow(() -> new ResourceNotFoundException("No post present with idPost = " + idPost));

		boolean isUserAnonymous = LoggedInUserUtils.checkIfUserIsAnonymous();
		PostView postView = new PostView();
		postView.setDateCreated(LocalDateTime.now());
		postView.setPost(post);
		if (!isUserAnonymous) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User loggedInUser = (User) authentication.getPrincipal();
			isFavorited = postRepository.findFavorite(loggedInUser.getIdUser(), idPost);
			isBookmarked = postRepository.findBookmark(loggedInUser.getIdUser(), idPost);
			
			postView.setViewer(loggedInUser);
		}
		postViewRepository.save(postView);
		Long countFavorites = postRepository.countFavorites(idPost);
		Long countBookmarks = postRepository.countBookmarks(idPost);
		return new PostResponseWithCommentsDTO(post, isBookmarked == 1 ? true : false, isFavorited == 1 ? true : false,
				countFavorites, countBookmarks, post.getPhoto());
	}


	public List<PostResponseDTO> findAllBookmark() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final User loggedInUser = (User) authentication.getPrincipal();
		if (authentication == null || loggedInUser == null) {
			throw new ResourceNotFoundException("User not authenticated.");
		}
		return postRepository.findAllBookmark(loggedInUser.getIdUser()).stream().map(post -> {
			Long isFavorited = loggedInUser != null
					? postRepository.findFavorite(loggedInUser.getIdUser(), post.getIdPost())
					: 0l;
			Long isBookmarked = loggedInUser != null
					? postRepository.findBookmark(loggedInUser.getIdUser(), post.getIdPost())
					: 0l;
			Long countFavorites = postRepository.countFavorites(post.getIdPost());
			Long countBookmarks = postRepository.countBookmarks(post.getIdPost());
			return new PostResponseDTO(post, isBookmarked == 1 ? true : false, isFavorited == 1 ? true : false,
					countFavorites, countBookmarks, post.getPhoto());
		}).collect(Collectors.toList());
	}
	

	public User getLoggedInUser() {
		if (!LoggedInUserUtils.checkIfUserIsAnonymous()) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			return (User) authentication.getPrincipal();
		}
		return null;
	}
	@Transactional
	public String toggleFavorite(Long idPost) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		if (authentication == null || user == null) {
			throw new ResourceNotFoundException("User not authenticated.");
		}
		if (postRepository.checkIfExistsPost(idPost) != 1) {
			throw new ResourceNotFoundException("No post present with idPost = " + idPost);
		}
		Long idUser = user.getIdUser();
		Long response = this.postRepository.findFavorite(idUser, idPost);
		if (response == 1) {
			this.postRepository.deleteFavoriteByIdUserByIdPost(idUser, idPost);
			return "Removed Favorite of idPost = " + idPost + " From idUser = " + idUser + ".";
		}
		if (response == 0) {
			this.postRepository.addFavoriteByIdUserByIdPost(idUser, idPost);
			return "Added Favorite of idPost = " + idPost + " From idUser = " + idUser + ".";
		}
		throw new IllegalArgumentException();
	}
	
	@Transactional
	public String toggleBookmark(Long idPost) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		if (authentication == null || user == null) {
			throw new ResourceNotFoundException("User not authenticated.");
		}
		if (postRepository.checkIfExistsPost(idPost) != 1) {
			throw new ResourceNotFoundException("No post present with idPost = " + idPost);
		}
		Long idUser = user.getIdUser();
		Long response = this.postRepository.findBookmark(idUser, idPost);
		if (response == 1) {
			this.postRepository.deleteBookmarkByIdUserByIdPost(idUser, idPost);
			return "Removed Bookmark of idPost = " + idPost + " From idUser = " + idUser + ".";
		}
		if (response == 0) {
			this.postRepository.addBookmarkByIdUserByIdPost(idUser, idPost);
			return "Added Bookmark of idPost = " + idPost + " From idUser = " + idUser + ".";
		}
		throw new IllegalArgumentException();
	}

	public List<PostTendencyDTO> findTendencyPosts() {
		List<PostTendencyDTO> postTendencyDTOList = new ArrayList<>();
		Long sevenDaysInMillis = 604800000l;
	    LocalDateTime startDate = LocalDateTime.now().minus(sevenDaysInMillis, ChronoUnit.MILLIS);
	    List<Long> idPostOfTop6Posts = postViewRepository.findTop6Posts(startDate);
	    idPostOfTop6Posts.forEach(idPost -> {
	    	Post post = postRepository.findById(idPost)
					.orElseThrow(() -> new ResourceNotFoundException("No post present with idPost = " + idPost));
	    	postTendencyDTOList.add(new PostTendencyDTO(post));
	    });
		return postTendencyDTOList;
	}

	public List<PostGridDTO> findAllByLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new ResourceNotFoundException("User not authenticated.");
		}
		User user = (User) authentication.getPrincipal();
		Long idUser = user.getIdUser();
		for (Role userRole : user.getRoles()) {
			if (userRole.getAuthority().equals("ROLE_ADMIN")) {
				idUser = null;
			}
		}
		logger.info("Listing all posts made by User[id=" + user.getIdUser() + ", name=" + user.getFullName() + ", email=" + user.getEmail() + "]");
		return this.postRepository.findAllByLoggedInUser(idUser);
	}

	@Transactional
	public void deleteByIdPost(Long idPost) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new ResourceNotFoundException("User not authenticated.");
		}
		User user = (User) authentication.getPrincipal();
		Post post = postRepository.findById(idPost)
				.orElseThrow(() -> new ResourceNotFoundException("No post present with idPost = " + idPost));
		boolean isAdmin = false;
		for (Role userRole : user.getRoles()) {
			if (userRole.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
			}
		}
		if (post.getAuthor().getFullName().equals(user.getFullName()) || isAdmin) {
			this.commentRepository.deleteAllByIdPost(idPost);
			this.postViewRepository.deleteAllByIdPost(idPost);
			this.postFavoriteRepository.deleteAllByIdPost(idPost);
			this.postRepository.deleteBookmarkByIdPost(idPost);
			this.postRepository.deleteById(idPost);
			logger.info("Deleted post of id=" + idPost + " by User of idUser=" + user.getIdUser());
		} else {
			throw new ResourceNotFoundException("Not authorized.");
		}
	}

}
