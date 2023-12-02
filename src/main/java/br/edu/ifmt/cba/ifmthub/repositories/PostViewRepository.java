package br.edu.ifmt.cba.ifmthub.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmt.cba.ifmthub.model.PostView;

@Repository
public interface PostViewRepository extends JpaRepository<PostView, Long>{

	@Query("SELECT pv.post.idPost "
			+ "FROM PostView pv "
			+ "WHERE pv.dateCreated >= :startDate "
			+ "GROUP BY pv.post.idPost "
			+ "ORDER BY COUNT(pv.idPostView) DESC "
			+ "LIMIT 6")
	List<Long> findTop6Posts(LocalDateTime startDate);

	@Modifying
	@Query(value = "delete from PostView pv "
			+ "WHERE pv.post.idPost = :idPost ")
	void deleteAllByIdPost(Long idPost);
	
	//long sevenDaysInMillis = 7L * 24L * 60L * 60L * 1000L; // 7 dias em milissegundos
    //Date startDate = new Date(System.currentTimeMillis() - sevenDaysInMillis);
    
}
