package br.edu.ifmt.cba.ifmthub;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.edu.ifmt.cba.ifmthub.model.Post;
import br.edu.ifmt.cba.ifmthub.model.Tag;
import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.repositories.PostRepository;
import br.edu.ifmt.cba.ifmthub.repositories.TagRepository;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;

@SpringBootApplication
public class IfmthubApplication implements CommandLineRunner {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(IfmthubApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Tag tag1 = new Tag(null, "Tecnologia", LocalDateTime.now(), true);
		tagRepository.save(tag1);
		Tag tag2 = new Tag(null, "História", LocalDateTime.now(), true);
		tagRepository.save(tag2);
		Post post1 = new Post(null, null, null, "Título do post",
				"Conteudo do post", LocalDateTime.now(),
				"url da imagem do post", true);
		post1.addTag(tag1, tag2);
		postRepository.save(post1);
		
		User user1 = new User();
		user1.setFullName("Rafael");
		userRepository.save(user1);
	}

}
