package br.edu.ifmt.cba.ifmthub.resources;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import br.edu.ifmt.cba.ifmthub.repositories.RoleRepository;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;
import br.edu.ifmt.cba.ifmthub.utils.EmailConfirmationEncryption;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
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
		System.out.println("usernamePassword:" + usernamePassword.toString());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		System.out.println("auth:" + auth.toString());
		User user = (User) auth.getPrincipal();
		System.out.println("user:" + user.toString());
		Map<String, Object> response = new HashMap<>();
		if (user.isAccountConfirmed()) {
			var token = tokenService.generateToken(user);
			response.put("token", token);
			return ResponseEntity.ok(response);
		} else {
			response.put("message", "Account not confirmed");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@Transactional
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
		if (this.repository.findByEmail(data.getEmail()) != null) {
			return ResponseEntity.badRequest().body(Map.of("message", "An account with this E-mail already exists."));
		}
		String encryptedPassword = passwordEncoder.encode(data.getPassword());
		User newUser = new User();
		newUser.setPassword(encryptedPassword);
		newUser.setBirthDate(data.getBirthDate());
		newUser.setDateCreated(LocalDateTime.now());
		newUser.setEmail(data.getEmail());
		newUser.setFullName(data.getFullName());
		newUser.setGender(data.getGender());
		newUser.setStatus(true);
		newUser.setAccountConfirmed(false);
		newUser.setUrlImgProfile(data.getUrlImgProfile());
		Role role = roleRepository.findByAuthority("ROLE_STUDENT");
		newUser.addRole(role);
		this.repository.save(newUser);
		sendConfirmationEmail(newUser);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/confirm")
	public ResponseEntity<Map<String, Object>> confirmAccount(@RequestParam String token) {
		try {
			String decryptedEmail = EmailConfirmationEncryption.decryptString(token);
			User userFound = repository.findByEmail(decryptedEmail);
			if (userFound != null) {
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
