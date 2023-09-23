package br.edu.ifmt.cba.ifmthub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
	
	@Query("select t from Tag t where UPPER(t.description) = UPPER(:description) ")
	Optional<Tag> findByDescription(String description);

}
