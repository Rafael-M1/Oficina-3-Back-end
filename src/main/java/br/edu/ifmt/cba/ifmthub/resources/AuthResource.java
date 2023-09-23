package br.edu.ifmt.cba.ifmthub.resources;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.edu.ifmt.cba.ifmthub.model.dto.LoginResponseDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.RegisterDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.UserDTO;
import br.edu.ifmt.cba.ifmthub.repositories.RoleRepository;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;
import br.edu.ifmt.cba.ifmthub.utils.EmailConfirmationEncryption;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
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
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		var token = tokenService.generateToken((User) auth.getPrincipal());
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}

	@Transactional
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
		if (this.repository.findByEmail(data.getEmail()) != null) {
			return ResponseEntity.badRequest().build();
		}
		String encryptedPassword = passwordEncoder.encode(data.getPassword());
		User newUser = new User();
		newUser.setPassword(encryptedPassword);
		newUser.setBirthDate(data.getBirthDate());
		newUser.setDateCreated(LocalDateTime.now());
		newUser.setEmail(data.getEmail());
		newUser.setFullName(data.getFullName());
		newUser.setGender('M');
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
	public ResponseEntity<String> confirmAccount(@RequestParam String token) {
		try {
			String decryptedEmail = EmailConfirmationEncryption.decryptString(token);
			User userFound = repository.findByEmail(decryptedEmail);
			if (userFound != null) {				
				userFound.setAccountConfirmed(true);
				repository.save(userFound);
				return ResponseEntity.ok("Account confirmed:" + decryptedEmail);
			}
			return ResponseEntity.badRequest().body("Invalid link.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	private void sendConfirmationEmail(User newUser) {
		try {
			String confirmationToken = EmailConfirmationEncryption.encryptString(newUser.getEmail());
			String host = "http://localhost:8080/";
			String link = host + "auth/confirm?token=" + confirmationToken;

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
			String sendgridApiKey = System.getenv("SENDGRID_API_KEY");
			
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
