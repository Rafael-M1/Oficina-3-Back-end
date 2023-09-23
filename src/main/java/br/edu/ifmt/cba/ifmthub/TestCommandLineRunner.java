package br.edu.ifmt.cba.ifmthub;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.edu.ifmt.cba.ifmthub.model.Category;
import br.edu.ifmt.cba.ifmthub.model.Course;
import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.model.Role;
import br.edu.ifmt.cba.ifmthub.model.Tag;
import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.repositories.RoleRepository;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;
import br.edu.ifmt.cba.ifmthub.services.CategoryService;
import br.edu.ifmt.cba.ifmthub.services.CourseService;
import br.edu.ifmt.cba.ifmthub.services.PostService;
import br.edu.ifmt.cba.ifmthub.services.TagService;
import br.edu.ifmt.cba.ifmthub.utils.EmailConfirmationEncryption;

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
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		Course course = new Course(null, "Técnologo de Sistemas para Internet", LocalDateTime.now(), true);
		courseService.save(course);
		courseService.save(new Course(null, "Técnologo de Análise e Desenvolvimento de Sistemas", LocalDateTime.now(), true));
		tagService.save(new Tag(null, "Tecnologia", LocalDateTime.now(), true));
		tagService.save(new Tag(null, "História", LocalDateTime.now(), true));
		roleRepository.save(new Role(null, "ROLE_ADMIN"));
		roleRepository.save(new Role(null, "ROLE_STUDENT"));
		
		User user1 = new User();
		user1.addCourse(course);
		user1.setFullName("Rafael");
		user1.setGender('M');
		user1.setBirthDate(LocalDate.now());
		user1.setDateCreated(LocalDateTime.now());
		user1.setUrlImgProfile("https://img.freepik.com/vetores-gratis/ilustracao-em-desenho-animado-de-astronauta-super-flying_138676-3259.jpg");
		user1.setEmail("rafael@gmail.com");
		user1.setPassword(passwordEncoder.encode("123456"));
		Role role1 = roleRepository.findByAuthority("ROLE_ADMIN");
		Role role2 = roleRepository.findByAuthority("ROLE_STUDENT");
		user1.addRole(role1, role2);
		userRepository.save(user1);
		
		Category category = new Category(null, "Tecnologia da Informação", LocalDateTime.now(), true);
		categoryService.save(category);
		
		Post post1 = new Post(null, user1, category, "Título do post",
				"Conteudo do post", LocalDateTime.now(),
				"url da imagem do post", true);
		post1.addTag(tagService.findById(1l), tagService.findById(2l));
		postService.save(post1);
		
		/*
		String sendgridApiKey = System.getenv("SENDGRID_API_KEY");
		System.out.println(sendgridApiKey);
		String email = "rafael@gmail.com";

        // Criptografe o email com a chave fixa
        String encryptedEmail = EmailConfirmationEncryption.encryptString(email);

        // Descriptografe o email com a chave fixa
        String decryptedEmail = EmailConfirmationEncryption.decryptString(encryptedEmail);

        System.out.println("Email original: " + email);
        System.out.println("Email criptografado: " + encryptedEmail);
        System.out.println("Email descriptografado: " + decryptedEmail);*/
	}
}
