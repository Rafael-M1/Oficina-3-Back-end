package br.edu.ifmt.cba.ifmthub.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;
import br.edu.ifmt.cba.ifmthub.resources.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	public User save(User user) {
		user.setDateCreated(LocalDateTime.now());
		user.setStatus(true);
		return userRepository.save(user);
	}

	public User findById(Long idUser) {
		return userRepository.findById(idUser)
				.orElseThrow(() -> new ResourceNotFoundException("No user present with idUser = " + idUser));
	}

	@Transactional
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Credentials"));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User update(User user) {
		User userSaved = this.findById(user.getIdUser());
		userSaved.setFullName(user.getFullName());
		userSaved.setGender(user.getGender());
		userSaved.setStatus(user.isStatus());
		userSaved.setUrlImgProfile(user.getUrlImgProfile());
		userRepository.save(userSaved);
		return userSaved;
	}

	public void delete(Long idUser) {
		userRepository.deleteById(idUser);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
	}
}
