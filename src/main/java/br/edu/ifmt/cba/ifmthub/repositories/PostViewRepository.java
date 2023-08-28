package br.edu.ifmt.cba.ifmthub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.PostView;

@Repository
public interface PostViewRepository extends JpaRepository<PostView, Long>{

}
