package br.edu.ifmt.cba.ifmthub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	@Query("select p from Post p where UPPER(p.title) like CONCAT('%', UPPER(:query), '%') ")
	List<Post> findAllFilteredByQueryText(String query);

}
