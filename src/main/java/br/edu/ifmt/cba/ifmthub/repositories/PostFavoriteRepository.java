package br.edu.ifmt.cba.ifmthub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.PostFavorite;
import br.edu.ifmt.cba.ifmthub.model.compositekeys.PostFavoriteId;

@Repository
public interface PostFavoriteRepository extends JpaRepository<PostFavorite, PostFavoriteId>{

}
