package molab.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import molab.util.Status;

@Entity
public class Address {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private Double qty;

	@Column(name = "create_time", nullable = false)
	private Long createTime;

	@Column(name = "invalid_time", nullable = false)
	private Long invalidTime;

	@Column(name = "sync_time")
	private Long syncTime;

	@Column(nullable = false)
	private Integer state;

	public Address() {
	};

	/**
	 * @deprecated
	 */
	public Address(String address) {
		this.address = address;
		this.qty = 0.0;
		this.createTime = System.currentTimeMillis();
		this.invalidTime = System.currentTimeMillis();
		this.state = Status.Common.CLOSE.getInt();
	}

	public Address(String address, Integer state) {
		this.address = address;
		this.qty = 0.0;
		this.createTime = System.currentTimeMillis();
		this.invalidTime = System.currentTimeMillis();
		this.state = state;
	}

	public Address(Integer state) {
		this.state = state;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the qty
	 */
	public Double getQty() {
		return qty;
	}

	/**
	 * @param qty
	 *            the qty to set
	 */
	public void setQty(Double qty) {
		this.qty = qty;
	}

	/**
	 * @return the createTime
	 */
	public Long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the invalidTime
	 */
	public Long getInvalidTime() {
		return invalidTime;
	}

	/**
	 * @param invalidTime
	 *            the invalidTime to set
	 */
	public void setInvalidTime(Long invalidTime) {
		this.invalidTime = invalidTime;
	}

	/**
	 * @return the syncTime
	 */
	public Long getSyncTime() {
		return syncTime;
	}

	/**
	 * @param syncTime
	 *            the syncTime to set
	 */
	public void setSyncTime(Long syncTime) {
		this.syncTime = syncTime;
	}

	/**
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}

}
