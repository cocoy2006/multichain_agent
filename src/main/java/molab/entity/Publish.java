package molab.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import molab.util.Status;

@Entity
public class Publish {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false)
	private String fromAddress;

	@Column(nullable = false)
	private String stream;

	@Column(nullable = false)
	private String key_;

	@Column(nullable = false)
	private String value;

	@Column(nullable = false)
	private Long createTime;

	@Column
	private Long blockTime;

	@Column(nullable = false)
	private Integer state;

	public Publish() {
	};

	public Publish(String fromAddress, String key, String value) {
		this.fromAddress = fromAddress;
		this.stream = "root";
		this.key_ = key;
		this.value = value;
		this.createTime = System.currentTimeMillis();
		this.state = Status.Common.OPEN.getInt();
	}

	public Publish(Integer state) {
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
	 * @return the stream
	 */
	public String getStream() {
		return stream;
	}

	/**
	 * @param stream
	 *            the stream to set
	 */
	public void setStream(String stream) {
		this.stream = stream;
	}

	/**
	 * @return the key_
	 */
	public String getKey_() {
		return key_;
	}

	/**
	 * @param key_
	 *            the key_ to set
	 */
	public void setKey_(String key_) {
		this.key_ = key_;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
