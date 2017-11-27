package molab.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import molab.util.Status;

@Entity
public class Token {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false)
	private String token;

	@Column(nullable = false)
	private Integer count;

	@Column(name = "create_time", nullable = false)
	private Long createTime;

	@Column(name = "invalid_time")
	private Long invalidTime;

	@Column(nullable = false)
	private Integer state;

	public Token() {
	}

	public Token(String token) {
		this.token = token;
		this.count = 0;
		this.createTime = System.currentTimeMillis();
		this.state = Status.Common.OPEN.getInt();
	}
	
	public Token(Integer state) {
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
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
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
