package molab.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import molab.entity.Publish;

@Repository
public interface PublishDao extends JpaRepository<Publish, Integer> {
	
	@Modifying
	@Transactional
	@Query("update Publish set blockTime = :blockTime, state = :state where id = :id")
	public void update(@Param("id") Integer id, @Param("blockTime") Long blockTime, @Param("state") Integer state);
	
}
