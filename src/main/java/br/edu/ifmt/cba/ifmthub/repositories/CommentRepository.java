package br.edu.ifmt.cba.ifmthub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

}
