package br.edu.ifmt.cba.ifmthub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Query("Select u from User u where u.email = :email ")
	User findByEmail(String email);

}
