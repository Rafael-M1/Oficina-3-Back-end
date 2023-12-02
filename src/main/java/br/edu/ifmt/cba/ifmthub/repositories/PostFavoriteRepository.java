package br.edu.ifmt.cba.ifmthub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.PostFavorite;
import br.edu.ifmt.cba.ifmthub.model.compositekeys.PostFavoriteId;

@Repository
public interface PostFavoriteRepository extends JpaRepository<PostFavorite, PostFavoriteId>{
	@Query("SELECT pf FROM PostFavorite pf "
			+ "WHERE pf.idPostFavorite.idUser = :idUser "
			+ "AND pf.idPostFavorite.idPost = :idPost ")
	PostFavorite findByIdUserAndByIdPost(Long idUser, Long idPost);

	@Modifying
	@Query(value = "delete from PostFavorite pf "
			+ "WHERE pf.post.idPost = :idPost ")
	void deleteAllByIdPost(Long idPost);
}
