package br.edu.ifmt.cba.ifmthub;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.edu.ifmt.cba.ifmthub.model.Category;
import br.edu.ifmt.cba.ifmthub.model.Course;
import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.model.Tag;
import br.edu.ifmt.cba.ifmthub.model.User;
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
	private UserRepository userRepository;
	
	@Override
	public void run(String... args) throws Exception {
		Course course = new Course(null, "Técnologo de Sistemas para Internet", LocalDateTime.now(), true);
		courseService.save(course);
		courseService.save(new Course(null, "Técnologo de Análise e Desenvolvimento de Sistemas", LocalDateTime.now(), true));
		tagService.save(new Tag(null, "Tecnologia", LocalDateTime.now(), true));
		tagService.save(new Tag(null, "História", LocalDateTime.now(), true));
		
		User user1 = new User();
		user1.addCourse(course);
		user1.setFullName("Rafael");
		user1.setGender('M');
		user1.setDateCreated(LocalDateTime.now());
		user1.setUrlImgProfile("url");
		userRepository.save(user1);
		
		Category category = new Category(null, "Tecnologia da Informação", LocalDateTime.now(), true);
		categoryService.save(category);
		
		Post post1 = new Post(null, user1, category, "Título do post",
				"Conteudo do post", LocalDateTime.now(),
				"url da imagem do post", true);
		post1.addTag(tagService.findById(1l), tagService.findById(2l));
		postService.save(post1);
	}
}
