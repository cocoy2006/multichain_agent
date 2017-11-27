package molab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

	@Value("${rpcurl}")
	private String rpcurl;

	@Value("${rpcuser}")
	private String rpcuser;

	@Value("${rpcpassword}")
	private String rpcpassword;

	@Value("${server.ip}")
	private String serverIp;

	@Value("${appsecret}")
	private String appsecret;

	@Value("${asset}")
	private String asset;

	@Value("${first.address}")
	private String firstAddress;

	@Value("${cpath}")
	private String cpath;

	@Value("${hpath}")
	private String hpath;

	@Value("${address.pool.min}")
	private Integer minAddressPool;

	@Value("${address.pool.max}")
	private Integer maxAddressPool;

	/**
	 * @return the rpcurl
	 */
	public String getRpcurl() {
		return rpcurl;
	}

	/**
	 * @param rpcurl
	 *            the rpcurl to set
	 */
	public void setRpcurl(String rpcurl) {
		this.rpcurl = rpcurl;
	}

	/**
	 * @return the rpcuser
	 */
	public String getRpcuser() {
		return rpcuser;
	}

	/**
	 * @param rpcuser
	 *            the rpcuser to set
	 */
	public void setRpcuser(String rpcuser) {
		this.rpcuser = rpcuser;
	}

	/**
	 * @return the rpcpassword
	 */
	public String getRpcpassword() {
		return rpcpassword;
	}

	/**
	 * @param rpcpassword
	 *            the rpcpassword to set
	 */
	public void setRpcpassword(String rpcpassword) {
		this.rpcpassword = rpcpassword;
	}

	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		return serverIp;
	}

	/**
	 * @param serverIp
	 *            the serverIp to set
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	/**
	 * @return the appsecret
	 */
	public String getAppsecret() {
		return appsecret;
	}

	/**
	 * @param appsecret
	 *            the appsecret to set
	 */
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	/**
	 * @return the asset
	 */
	public String getAsset() {
		return asset;
	}

	/**
	 * @param asset
	 *            the asset to set
	 */
	public void setAsset(String asset) {
		this.asset = asset;
	}

	/**
	 * @return the firstAddress
	 */
	public String getFirstAddress() {
		return firstAddress;
	}

	/**
	 * @param firstAddress
	 *            the firstAddress to set
	 */
	public void setFirstAddress(String firstAddress) {
		this.firstAddress = firstAddress;
	}

	/**
	 * @return the cpath
	 */
	public String getCpath() {
		return cpath;
	}

	/**
	 * @param cpath
	 *            the cpath to set
	 */
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	/**
	 * @return the hpath
	 */
	public String getHpath() {
		return hpath;
	}

	/**
	 * @param hpath
	 *            the hpath to set
	 */
	public void setHpath(String hpath) {
		this.hpath = hpath;
	}

	/**
	 * @return the minAddressPool
	 */
	public Integer getMinAddressPool() {
		return minAddressPool;
	}

	/**
	 * @param minAddressPool
	 *            the minAddressPool to set
	 */
	public void setMinAddressPool(Integer minAddressPool) {
		this.minAddressPool = minAddressPool;
	}

	/**
	 * @return the maxAddressPool
	 */
	public Integer getMaxAddressPool() {
		return maxAddressPool;
	}

	/**
	 * @param maxAddressPool
	 *            the maxAddressPool to set
	 */
	public void setMaxAddressPool(Integer maxAddressPool) {
		this.maxAddressPool = maxAddressPool;
	}

}
