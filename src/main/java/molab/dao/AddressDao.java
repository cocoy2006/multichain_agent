package molab.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import molab.entity.Address;

@Repository
public interface AddressDao extends JpaRepository<Address, Integer> {
	
	public Address findByAddress(String address);

	public Address findByAddressAndState(String address, Integer state);

	public List<Address> findBySyncTimeAndState(Long syncTime, Integer state);

	@Query(value = "select sum(qty) from Address where state = :state")
	public Double findSumQty(@Param("state") Integer state);

	@Modifying
	@Transactional
	@Query("update Address set syncTime = null where id = :id")
	public void update(@Param("id") Integer id);

	@Modifying
	@Transactional
	@Query("update Address set qty = :qty where id = :id")
	public void update(@Param("id") Integer id, @Param("qty") Double qty);

	@Modifying
	@Transactional
	@Query("update Address set qty = :qty, syncTime = :syncTime where id = :id")
	public void update(@Param("id") Integer id, @Param("qty") Double qty, @Param("syncTime") Long syncTime);

	@Modifying
	@Transactional
	@Query("update Address set state = :state where id = :id")
	public void update(@Param("id") Integer id, @Param("state") Integer state);

}
