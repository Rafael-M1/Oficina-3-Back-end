package br.edu.ifmt.cba.ifmthub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	@Query("select c from Category c where UPPER(c.description) = UPPER(:description) ")
	Optional<Category> findByDescription(String description);
}
