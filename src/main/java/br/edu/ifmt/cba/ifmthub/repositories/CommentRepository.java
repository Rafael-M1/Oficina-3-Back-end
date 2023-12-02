package br.edu.ifmt.cba.ifmthub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

	@Modifying
	@Query(value = "delete from Comment c "
			+ "WHERE c.post.idPost = :idPost ")
	void deleteAllByIdPost(Long idPost);

}
