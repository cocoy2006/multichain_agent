package molab.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import molab.util.Status;

@Entity
public class Asset {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false)
	private String fromAddress;

	@Column(nullable = false)
	private String toAddress;

	@Column(nullable = false)
	private Double qty;

	@Column(nullable = false)
	private Long createTime;

	@Column
	private Long blockTime;

	@Column(nullable = false)
	private Integer state;

	public Asset() {
	};

	public Asset(String fromAddress, String toAddress, Double qty) {
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.qty = qty;
		this.createTime = System.currentTimeMillis();
		this.state = Status.Common.OPEN.getInt();
	}
	
	public Asset(Integer state) {
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
	 * @return the fromAddress
	 */
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * @param fromAddress
	 *            the fromAddress to set
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * @return the toAddress
	 */
	public String getToAddress() {
		return toAddress;
	}

	/**
	 * @param toAddress
	 *            the toAddress to set
	 */
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
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
	 * @return the blockTime
	 */
	public Long getBlockTime() {
		return blockTime;
	}

	/**
	 * @param blockTime
	 *            the blockTime to set
	 */
	public void setBlockTime(Long blockTime) {
		this.blockTime = blockTime;
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
