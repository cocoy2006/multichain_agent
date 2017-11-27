package molab.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import molab.entity.Total;

@Repository
public interface TotalDao extends JpaRepository<Total, Integer> {

	@Modifying
	@Transactional
	@Query("update Total set qty = :qty where id = 1")
	void update(@Param("qty") double qty);
	
}
