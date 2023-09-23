package br.edu.ifmt.cba.ifmthub.resources;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifmt.cba.ifmthub.configs.TokenService;
import br.edu.ifmt.cba.ifmthub.model.Role;
import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.model.dto.LoginResponseDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.RegisterDTO;
import br.edu.ifmt.cba.ifmthub.model.dto.UserDTO;
import br.edu.ifmt.cba.ifmthub.repositories.RoleRepository;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;
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
		newUser.setUrlImgProfile(data.getUrlImgProfile());
		Role role = roleRepository.findByAuthority("ROLE_STUDENT");
		newUser.addRole(role);
		this.repository.save(newUser);

		return ResponseEntity.ok().build();
	}
}
