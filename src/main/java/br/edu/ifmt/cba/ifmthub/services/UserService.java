package br.edu.ifmt.cba.ifmthub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}

	public User findById(Long idUser) {
		return userRepository.findById(idUser).get();
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User update(User user) {
		User userSaved = userRepository.findById(user.getIdUser()).get();
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
}
