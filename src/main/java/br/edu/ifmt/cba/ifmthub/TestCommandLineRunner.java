package br.edu.ifmt.cba.ifmthub;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.edu.ifmt.cba.ifmthub.model.Category;
import br.edu.ifmt.cba.ifmthub.model.Course;
import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.model.PostFavorite;
import br.edu.ifmt.cba.ifmthub.model.Role;
import br.edu.ifmt.cba.ifmthub.model.Tag;
import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.model.compositekeys.PostFavoriteId;
import br.edu.ifmt.cba.ifmthub.repositories.PostFavoriteRepository;
import br.edu.ifmt.cba.ifmthub.repositories.PostRepository;
import br.edu.ifmt.cba.ifmthub.repositories.RoleRepository;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;
import br.edu.ifmt.cba.ifmthub.services.CategoryService;
import br.edu.ifmt.cba.ifmthub.services.CourseService;
import br.edu.ifmt.cba.ifmthub.services.PostService;
import br.edu.ifmt.cba.ifmthub.services.TagService;

@Component
@Profile("test")
public class TestCommandLineRunner implements CommandLineRunner {

	@Autowired
	private TagService tagService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private PostService postService;
	
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PostFavoriteRepository postFavoriteRepository;

	@Override
	public void run(String... args) throws Exception {
		Course course = new Course(null, "Técnologo de Sistemas para Internet", LocalDateTime.now(), true);
		courseService.save(course);
		courseService.save(
				new Course(null, "Técnologo de Análise e Desenvolvimento de Sistemas", LocalDateTime.now(), true));
		tagService.save(new Tag(null, "Tecnologia", LocalDateTime.now(), true));
		tagService.save(new Tag(null, "História", LocalDateTime.now(), true));
		roleRepository.save(new Role(null, "ROLE_ADMIN"));
		roleRepository.save(new Role(null, "ROLE_STUDENT"));
		Role role1 = roleRepository.findByAuthority("ROLE_ADMIN");
		Role role2 = roleRepository.findByAuthority("ROLE_STUDENT");

		// Usuario com email confirmado
		User user1 = new User();
		user1.addCourse(course);
		user1.setStatus(true);
		user1.setAccountConfirmed(true);
		user1.setFullName("Rafael");
		user1.setGender("M");
		user1.setBirthDate(LocalDate.now());
		user1.setDateCreated(LocalDateTime.now());
		user1.setUrlImgProfile(
				"https://img.freepik.com/vetores-gratis/ilustracao-em-desenho-animado-de-astronauta-super-flying_138676-3259.jpg");
		user1.setEmail("rafael@gmail.com");
		user1.setPassword(passwordEncoder.encode("123456"));
		user1.addRole(role1, role2);
		userRepository.save(user1);

		// Usuario sem email confirmado
		User user2 = new User();
		user2.addCourse(course);
		user2.setStatus(true);
		user2.setAccountConfirmed(false);
		user2.setFullName("Maria");
		user2.setGender("F");
		user2.setBirthDate(LocalDate.now());
		user2.setDateCreated(LocalDateTime.now());
		user2.setUrlImgProfile(
				"https://img.freepik.com/vetores-gratis/ilustracao-em-desenho-animado-de-astronauta-super-flying_138676-3259.jpg");
		user2.setEmail("maria@gmail.com");
		user2.setPassword(passwordEncoder.encode("123456"));
		user2.addRole(role2);
		userRepository.save(user2);

		Category category = new Category(null, "Tecnologia da Informação", LocalDateTime.now(), true);
		categoryService.save(category);

		Post post1 = new Post(null, user1, category, "Título do post", "Subtítulo do post", "Conteudo do post", LocalDateTime.now(),
				"url da imagem do post", true);
		post1.addTag(tagService.findById(1l), tagService.findById(2l));
		postService.save(post1);
		
		user1.addBookMark(post1);
		userRepository.save(user1);
		
		PostFavoriteId pfi1 = new PostFavoriteId();
		pfi1.setIdPost(1l);
		pfi1.setIdUser(1l);
		
		Post post_PostFavorite = postRepository.findById(1l).get();
		PostFavorite pf1 = new PostFavorite();
		pf1.setUser(user1);
		pf1.setPost(post_PostFavorite);
		pf1.setDateCreated(LocalDateTime.now());
		pf1.setIdPostFavorite(pfi1);
		postFavoriteRepository.save(pf1);
		
		PostFavorite postFavoriteSaved = postFavoriteRepository.findByIdUserAndByIdPost(1l, 1l);
		//System.out.println(postFavoriteSaved);
	}
}
