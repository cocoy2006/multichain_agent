package molab.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import molab.entity.Asset;

@Repository
public interface AssetDao extends JpaRepository<Asset, Integer> {
	
	@Query(value = "select sum(qty) from Asset where createTime between :min and :max")
	public Double findSumQty(@Param("min") Long min, @Param("max") Long max);
	
	@Modifying
	@Transactional
	@Query("update Asset set blockTime = :blockTime, state = :state where id = :id")
	public void update(@Param("id") Integer id, @Param("blockTime") Long blockTime, @Param("state") Integer state);
	
}
