package br.edu.ifmt.cba.ifmthub.resources;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import br.edu.ifmt.cba.ifmthub.configs.TokenService;
import br.edu.ifmt.cba.ifmthub.model.Role;
import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.model.dto.LoginRequestDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.RegisterDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.UserDTO;
import br.edu.ifmt.cba.ifmthub.repositories.RoleRepository;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;
import br.edu.ifmt.cba.ifmthub.utils.EmailConfirmationEncryption;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	private static Logger logger = LogManager.getLogger(AuthResource.class);
	
	@Value("${sendgrid.api.key}")
	private String sendgridApiKey;

	@Value("${api.backend.hostaddress}")
	private String hostAddress;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
		try {
			var auth = this.authenticationManager.authenticate(usernamePassword);
			User user = (User) auth.getPrincipal();
			Map<String, Object> response = new HashMap<>();
			if (user.isAccountConfirmed()) {
				var token = tokenService.generateToken(user);
				response.put("token", token);
				logger.info("User Logged in: User[" + user.getEmail() + ", " + user.getFullName() + "]");
				return ResponseEntity.ok(response);
			} else {
				response.put("message", "Account not confirmed");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message", "User does not exist or invalid credentials."));
		}

	}

	@Transactional
	@PostMapping(value = "/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {	
		Optional<User> userOpt = this.repository.findByEmail(data.getEmail());
		if (userOpt.isPresent()) {
			return ResponseEntity.badRequest().body(Map.of("message", "An account with this E-mail already exists."));
		}
		String encryptedPassword = passwordEncoder.encode(data.getPassword());
		User newUser = new User();
		newUser.setPassword(encryptedPassword);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate userBirthDate = LocalDate.parse(data.getBirthDate(), dtf);
		newUser.setBirthDate(userBirthDate);
		newUser.setDateCreated(LocalDateTime.now());
		newUser.setEmail(data.getEmail());
		newUser.setFullName(data.getFullName());
		newUser.setGender(data.getGender());
		newUser.setStatus(true);
		newUser.setAccountConfirmed(false);
		Role role = roleRepository.findByAuthority("ROLE_STUDENT");
		newUser.addRole(role);
		this.repository.save(newUser);
		sendConfirmationEmail(newUser);
		
		return ResponseEntity.ok().body(new UserDTO(newUser));
	}
	
	@Transactional
	@PostMapping(value = "/register/{idUser}", consumes = { "multipart/form-data" })
	public ResponseEntity register(@RequestParam(value = "file", required = true) MultipartFile file,
			@PathVariable Long idUser) {
		Optional<User> userOpt = this.repository.findById(idUser);
		if (userOpt.isPresent()) {
			try {
				User user = userOpt.get();
				user.setPhoto(file.getBytes());
				this.repository.save(user);
				return ResponseEntity.ok().body(new UserDTO(user));
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
	}

	@GetMapping("/confirm")
	public ResponseEntity<Map<String, Object>> confirmAccount(@RequestParam String token) {
		try {
			String decryptedEmail = EmailConfirmationEncryption.decryptString(token);
			Optional<User> userFoundOpt = repository.findByEmail(decryptedEmail);
			if (userFoundOpt.isPresent()) {
				User userFound = userFoundOpt.get();
				userFound.setAccountConfirmed(true);
				repository.save(userFound);
				Map<String, Object> response = new HashMap<>();
				response.put("message", "Account confirmed");
				return ResponseEntity.ok(response);
			}
			return ResponseEntity.badRequest().body(Map.of("message", "Invalid link."));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Map.of("message", "Invalid link."));
		}

	}

	private void sendConfirmationEmail(User newUser) {
		try {
			String confirmationToken = EmailConfirmationEncryption.encryptString(newUser.getEmail());
			String link = hostAddress + "/auth/confirm?token="
					+ URLEncoder.encode(confirmationToken, StandardCharsets.UTF_8.toString());
			String htmlContent = "<p>Ol&aacute;,&nbsp;" + newUser.getFullName() + "!</p>" + "<p>&nbsp;</p>"
					+ "<p>Sua conta no IFMT Hub est&aacute; quase pronta. Para ativ&aacute;-la, por favor confirme o seu endere&ccedil;o de email clicando no link abaixo.</p>"
					+ "<p><a href=\"" + link + "\">" + link + "</a></p>" + "<p>&nbsp;</p>"
					+ "<p>Sua conta n&atilde;o ser&aacute; ativada at&eacute; que seu email seja confirmado.</p>"
					+ "<p>Se voc&ecirc; n&atilde;o se cadastrou&nbsp;no IFMT Hub recentemente, por favor ignore este email.</p>"
					+ "<p>&nbsp;</p>" + "<p>Atenciosamente,</p>" + "<p>Equipe IFMT Hub.</p>";

			Email from = new Email("ifmthub@gmail.com");
			String subject = "IFMT Hub: confirmação de conta";
			Email to = new Email(newUser.getEmail());
			Content content = new Content("text/html", htmlContent);
			Mail mail = new Mail(from, subject, to, content);

			SendGrid sg = new SendGrid(sendgridApiKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
