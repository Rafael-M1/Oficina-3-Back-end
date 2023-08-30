package br.edu.ifmt.cba.ifmthub.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.services.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserResource {

	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<User> save(@RequestBody User user) {
		User userSaved = this.userService.save(user);
		return new ResponseEntity<User>(userSaved, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<User>> findAll() {
		List<User> userList = this.userService.findAll();
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
	}
	
	@GetMapping("/{idUser}")
	public ResponseEntity<User> findById(@PathVariable Long idUser) {
		User userFound = this.userService.findById(idUser);
		return new ResponseEntity<User>(userFound, HttpStatus.OK);
	}
}
