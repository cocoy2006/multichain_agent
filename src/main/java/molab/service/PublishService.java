package molab.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.google.gson.Gson;

import molab.Properties;
import molab.component.Data;
import molab.dao.AddressDao;
import molab.dao.AssetDao;
import molab.dao.PublishDao;
import molab.dao.TokenDao;
import molab.dao.TotalDao;
import molab.entity.Address;
import molab.entity.Asset;
import molab.entity.Publish;
import molab.util.Hex;
import molab.util.Molab;
import molab.util.RespUtil;
import molab.util.Status;

@Service
public class PublishService {

	private static final long SLEEP_TIME = 20L;
	private static boolean assetRunning = false;
	private static boolean publishRunning = false;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Properties properties;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private AssetDao assetDao;

	@Autowired
	private PublishDao publishDao;

	@Autowired
	private TokenDao tokenDao;

	@Autowired
	private TotalDao totalDao;

	private void asset(BitcoinJSONRPCClient client, double total) {
		if (!assetRunning) {
			assetRunning = true;
			// fetch data from db
			Example<Asset> example = Example.of(new Asset(Status.Common.OPEN.getInt()));
			List<Asset> list = assetDao.findAll(example);
			if (list != null && list.size() > 0) {
				String fromAddress = properties.getFirstAddress();
				String assetName = properties.getAsset();
				for (Asset asset : list) {
					try {
						String txid = client.sendAssetFrom(fromAddress, 
								asset.getToAddress(), assetName, asset.getQty());
						if (txid != null) { // saved to chain successfully
							assetDao.update(asset.getId(), System.currentTimeMillis(), Status.Common.CLOSE.getInt());
							// update total
							totalDao.update(Molab.rescale(total + asset.getQty()));
						}
						Thread.sleep(SLEEP_TIME);
					} catch (BitcoinException | InterruptedException e) {
						log.error(e.getMessage());
					}
				}
			}
			assetRunning = false;
		}
	}

	private void asynAsset(BitcoinJSONRPCClient client, String toAddress, double total, double qty)
			throws BitcoinException {
		String fromAddress = properties.getFirstAddress();
		// save to database first and sync to chain later
		Asset asset = new Asset(fromAddress, toAddress, qty);
		assetDao.save(asset);
		// update address' qty
		Address address = addressDao.findByAddressAndState(toAddress, Status.Common.OPEN.getInt());
		addressDao.update(address.getId(), address.getQty() + qty);
		// sync to chain in thread
		new Thread(new Runnable() {

			@Override
			public void run() {
				asset(client, total);
			}

		}).start();
	}

	private void asynPublish(BitcoinJSONRPCClient client, Data req) {
		// save to database first and sync to chain later
		String key = Molab.parseToday();
		String value = new Gson().toJson(req);
		Publish publish = new Publish(req.getAddress(), key, value);
		publishDao.save(publish);
		// sync to chain in thread
		new Thread(new Runnable() {

			@Override
			public void run() {
				publish(client);
			}

		}).start();
	}

	private boolean checkToken(String token) {
		return tokenDao.findByTokenAndState(token, Status.Common.OPEN.getInt()) != null;
	}

	private void publish(BitcoinJSONRPCClient client) {
		if (!publishRunning) {
			publishRunning = true;
			// fetch data from db
			Example<Publish> example = Example.of(new Publish(Status.Common.OPEN.getInt()));
			List<Publish> list = publishDao.findAll(example);
			if (list != null && list.size() > 0) {
				for (Publish publish : list) {
					try {
						String txid = client.publishFrom(publish.getFromAddress(), 
								Hex.str2HexStr(publish.getKey_()), Hex.str2HexStr(publish.getValue()));
						if (txid != null) { // saved to chain successfully
							publishDao.update(publish.getId(), System.currentTimeMillis(),
									Status.Common.CLOSE.getInt());
						}
						Thread.sleep(SLEEP_TIME);
					} catch (BitcoinException | InterruptedException e) {
						log.error(e.getMessage());
					}
				}
			}
			publishRunning = false;
		}
	}

	public String publish(String token, Data req) {
		if (!checkToken(token)) {
			return RespUtil.errorResp(Status.Err.ILLEGAL_TOKEN.getCode(), Status.Err.ILLEGAL_TOKEN.getMsg());
		}
		if (req == null || req.getAddress() == null || req.getTransid() == null || req.getTransid().length() >= 20) {
			return RespUtil.errorResp(Status.Err.ILLEGAL_PARAMETER.getCode(), Status.Err.ILLEGAL_PARAMETER.getMsg());
		}
		try {
			BitcoinJSONRPCClient client = BitcoinJSONRPCClient.getInstance(null, null, properties.getRpcurl());
			if (client != null) {
				// 1.保存消费记录
				asynPublish(client, req);
				// 2.分配huihong
				// 2.1.获取已分配的huihong情况
				double total = totalDao.findOne(1).getQty();
				// 2.2.根据积分兑换标准计算需奖励的huihong
				double reward = reward(total, req.getAmount());
				// 2.3.分配huihong并记录日志
				asynAsset(client, req.getAddress(), total, reward);
				// 3.返回结果
				Data data = new Data();
				data.setQty(reward);
				return RespUtil.successResp(data);
			}
		} catch (BitcoinException e) {
			log.error(e.getMessage());
		}
		return RespUtil.errorResp(Status.Err.SYSTEM_ERROR.getCode(), Status.Err.SYSTEM_ERROR.getMsg());
	}

	private double reward(double total, double amount) {
		double reward = 0;
		if (total <= 1000000000l) {
			reward = amount;
		} else if (total <= 2000000000l) {
			reward = amount / 10;
		} else if (total <= 4000000000l) {
			reward = amount / 50;
		} else if (total <= 8000000000l) {
			reward = amount / 100;
		} else if (total <= 9000000000l) {
			reward = amount / 300;
		} else {
			reward = amount / 600;
		}
		return reward;
	}

}
