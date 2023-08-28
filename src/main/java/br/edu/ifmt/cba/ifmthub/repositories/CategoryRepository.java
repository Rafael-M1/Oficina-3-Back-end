package br.edu.ifmt.cba.ifmthub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}
