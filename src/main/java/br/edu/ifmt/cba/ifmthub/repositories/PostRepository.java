package br.edu.ifmt.cba.ifmthub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	@Query("select p from Post p where UPPER(p.title) like CONCAT('%', UPPER(:query), '%') ")
	List<Post> findAllFilteredByQueryText(String query);
	
	@Query(value = "SELECT count(1) FROM tb_bookmark "
			+ "WHERE tb_bookmark.id_user = :idUser "
			+ "AND tb_bookmark.id_post = :idPost ",
			nativeQuery = true)
	Long findBookmark(Long idUser, Long idPost);
	
	@Query(value = "SELECT count(1) FROM tb_bookmark "
			+ "WHERE tb_bookmark.id_post = :idPost ",
			nativeQuery = true)
	Long countBookmarks(Long idPost);
	
	@Query(value = "SELECT count(1) FROM post_favorite "
			+ "WHERE post_favorite.id_user = :idUser "
			+ "AND post_favorite.id_post = :idPost ",
			nativeQuery = true)
	Long findFavorite(Long idUser, Long idPost);
	
	@Query(value = "SELECT count(1) FROM post_favorite "
			+ "WHERE post_favorite.id_post = :idPost ",
			nativeQuery = true)
	Long countFavorites(Long idPost);
	
	@Query(value = "SELECT count(1) FROM post "
			+ "WHERE post.id_post = :idPost ",
			nativeQuery = true)
	Long checkIfExistsPost(Long idPost);
	
	@Modifying
	@Query(value = "delete from tb_bookmark "
			+ "WHERE tb_bookmark.id_user = :idUser "
			+ "AND tb_bookmark.id_post = :idPost ",
			nativeQuery = true)
	void deleteBookmarkByIdUserByIdPost(Long idUser, Long idPost);
	@Modifying
	@Query(value = "insert into tb_bookmark (id_user, id_post) "
			+ "VALUES (:idUser, :idPost) ",
			nativeQuery = true)
	void addBookmarkByIdUserByIdPost(Long idUser, Long idPost);

	@Query("SELECT p FROM Post p "
			+ "JOIN p.usersBookmarks ub "
			+ "WHERE ub.idUser = :idUser ")
	List<Post> findAllBookmark(Long idUser);

}
