package molab.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import molab.entity.Token;

@Repository
public interface TokenDao extends JpaRepository<Token, Integer> {
	
	public Token findByTokenAndState(String token, Integer state);
	
	@Modifying
	@Transactional
	@Query("update Token set state = :state, invalidTime = :invalidTime where id = :id")
	public void update(@Param("id") Integer id, @Param("state") Integer state, @Param("invalidTime") Long invalidTime);
	
}
