package br.edu.ifmt.cba.ifmthub.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.model.dto.UserDTO;

@RestController
public class MeResource {

	@Transactional
	@GetMapping("/me")
	public ResponseEntity<UserDTO> getLoggedInUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal().equals("anonymousUser")) {
			User user = new User();
			user.setFullName("anonymousUser");
			return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
		}
		User user = (User) authentication.getPrincipal();
		UserDTO userDTO = new UserDTO(user);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}
}
