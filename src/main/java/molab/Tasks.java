package molab;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Balance;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

import molab.dao.AddressDao;
import molab.dao.AssetDao;
import molab.dao.TotalDao;
import molab.entity.Address;
import molab.entity.Total;
import molab.util.Hex;
import molab.util.Molab;
import molab.util.Status;

@Configuration
@EnableScheduling
public class Tasks {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final long SLEEP_TIME = 20L;
	private static final int MOD = 500;

	@Autowired
	private Properties properties;
	
	@Autowired
	private AddressDao addressDao;

	@Autowired
	private AssetDao assetDao;
	
	@Autowired
	private TotalDao totalDao;
	
	private void allocate(BitcoinJSONRPCClient client) {
		long startTime = System.currentTimeMillis();
		log.info("allocate start at " + new Date());
		Example<Address> example = Example.of(new Address(Status.Common.CLOSE.getInt()));
		int count = (int) addressDao.count(example);
		if(count < properties.getMinAddressPool()) {
			int number = properties.getMaxAddressPool() - count;
			log.info("allocate address is " + number);
			for(int i = 0; i < number; i++) {
				if(i % MOD == 0) {
					log.info("allocate processing " + i + " of " + number);
				}
				try {
					String address = client.getNewAddress();
					if(address != null) {
						// 新地址写入Address表
						Address addr = new Address(address, Status.Common.CLOSE.getInt());
						addressDao.save(addr);
					}
				} catch (BitcoinException e) {
					log.error(e.getMessage());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}
		long doneTime = System.currentTimeMillis();
		log.info("allocate done at " + new Date() + ", total time is " + (doneTime - startTime));
	}
	
	private Double balanceOfAddress(BitcoinJSONRPCClient client, String address) {
		List<Balance> list = null;
		try {
			list = client.getAddressBalances(address);
		} catch (BitcoinException e) {
			try {
				client.importAddress(address);
				list = client.getAddressBalances(address);
			} catch (BitcoinException e1) {
				log.error(address + " error: not exist!");
			}
		}
		// if not found, import from another wallet and try again
//		if(list == null || list.size() == 0) { 
//			client.importAddress(address);
//			list = client.getAddressBalances(address);
//		}
		if(list != null && list.size() > 0) {
			Balance b = list.get(0);
			return Molab.rescale(b.qty());
		}
		return 0.0;
	}
	
    private Double balanceOfTotal(BitcoinJSONRPCClient client) throws BitcoinException {
		Double left = balanceOfAddress(client, properties.getFirstAddress());
		List<Balance> list = client.getTotalBalances();
		if(list != null && list.size() > 0) {
			Balance b = list.get(0);
			return Molab.rescale(b.qty()) - left;
		}
		return 0.0;
	}
	
	private void cash(BitcoinJSONRPCClient client) throws IOException, BitcoinException {
		long startTime = System.currentTimeMillis();
		log.info("C start at " + new Date());
		// 统计当天的消费额
		double dayConsumption = assetDao.findSumQty(Molab.parseYesterdayStart(), Molab.parseYesterdayEnd());
		log.info("day consumption is " + dayConsumption);
		if(dayConsumption > 0) {
			// 全部已经分配的huihong的数量总和
			double total = balanceOfTotal(client);
			Example<Address> example = Example.of(new Address(Status.Common.OPEN.getInt()));
			List<Address> list = addressDao.findAll(example);
			if(list != null && list.size() > 0) {
				// 新建当日C文件yyyyMMdd.txt
				FileWriter writer = new FileWriter(properties.getCpath() + Molab.parseYesterday() + ".txt", true);
				for(int i = 0; i < list.size(); i++) {
					Address address = list.get(i);
					if(i % MOD == 0) {
						log.info("C processing " + i + " of " + list.size());
					}
					// 根据公式计算现金C=(慧美系统当天的消费额)*1%*(拥有的huihong数)/(全部已经分配的huihong数总和)
					double cash = Molab.rescale(dayConsumption * 0.01 * address.getQty() / total);
					if(cash > 0.01) {
						// 写入C文件
						writer.write(address.getAddress() + ":" + address.getQty() + ":" + cash + System.getProperty("line.separator"));
						try {
							// stream方式记录现金C, key=address, value=cash, time=blocktime
							client.publishFrom(address.getAddress(), Hex.str2HexStr(address.getAddress()), Hex.str2HexStr(String.valueOf(cash)));
						} catch (BitcoinException e) {
							log.error("Error happen to address " + address.getAddress());
						}
					}
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {}
				}
				writer.close();
			}
		}
		long doneTime = System.currentTimeMillis();
		log.info("C done at " + new Date() + ", total time is " + (doneTime - startTime));
    }
	
	private BitcoinJSONRPCClient getClient() {
//		Authenticator.setDefault(new Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(properties.getRpcuser(), properties.getRpcpassword().toCharArray());
//			}
//		});
//		try {
//			return new BitcoinJSONRPCClient(new URL(properties.getRpcurl()));
//		} catch (MalformedURLException e) {
//			log.error(e.getMessage());
//		}
//		return null;
		return BitcoinJSONRPCClient.getInstance(null, null, properties.getRpcurl());
	}
	
	private void huihong() {
		long startTime = System.currentTimeMillis();
		log.info("H start at " + new Date());
		try {
			Example<Address> example = Example.of(new Address(Status.Common.OPEN.getInt()));
			List<Address> list = addressDao.findAll(example);
			if(list != null && list.size() > 0) {
				// 新建当月H文件yyyyMM.txt
				FileWriter writer = new FileWriter(properties.getHpath() + Molab.parseMonth() + ".txt", true);
				for(int i = 0; i < list.size(); i++) {
					Address address = list.get(i);
					if(i % MOD == 0) {
						log.info("H processing " + i + " of " + list.size());
					}
					if(address.getQty() > 10000) {
						// 写入H文件
						writer.write(address.getAddress() + ":" + address.getQty() + System.getProperty("line.separator"));
					}
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {}
				}
				writer.close();
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		long doneTime = System.currentTimeMillis();
		log.info("H done at " + new Date() + ", total time is " + (doneTime - startTime));
	}
	
	// 以周为周期，每天获取1/7的address（略过的address会在下个周期同步）
	private void setTimeNull() {
		log.info("set_sync_time start.");
		Example<Address> example = Example.of(new Address(Status.Common.OPEN.getInt()));
		Pageable pageable = new PageRequest(0, (int) Math.ceil(addressDao.count(example) / 7d), Direction.ASC, "syncTime");
		Page<Address> page = addressDao.findAll(example, pageable);
		List<Address> list = page.getContent();
		log.info("set address: " + list.size());
		// address的sync_time设为null
//		StringBuffer sb = new StringBuffer("0");
//		for(Address address : list) {
//			sb.append(",").append(address.getId());
//		}
//		addressDao.update(sb.toString());
		for(Address address : list) {
			if(address.getSyncTime() != null) {
				addressDao.update(address.getId());
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {}
			}
		}
		log.info("set_sync_time done.");
	}
	
	/**
	 * 只同步sync_time为null的address */
	private void syncAddress(BitcoinJSONRPCClient client) {
		setTimeNull();
		long startTime = System.currentTimeMillis();
		log.info("sync address start at " + new Date());
		List<Address> list = addressDao.findBySyncTimeAndState(null, Status.Common.OPEN.getInt());
		log.info("sync address: " + list.size());
		for(int i = 0; i < list.size(); i++) {
			Address address = list.get(i);
			if(i % MOD == 0) {
				log.info("sync address processing " + i + " of " + list.size());
			}
			double balance = balanceOfAddress(client, address.getAddress());
			if(balance != address.getQty()) {
				log.warn(address.getAddress() + "'s qty was not synchronized. Update to " + balance);
				// 以区块链为准，更新数据库
				addressDao.update(address.getId(), balance, System.currentTimeMillis());
			}
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {}
		}
		long doneTime = System.currentTimeMillis();
		log.info("sync address done at " + new Date() + ", total time is " + (doneTime - startTime));
	}
	
	private void syncTotal(BitcoinJSONRPCClient client) {
		long startTime = System.currentTimeMillis();
		log.info("sync total start at " + new Date());
		try {
			double qty = balanceOfTotal(client);
			Total total = totalDao.findOne(1);
			if(total != null && total.getQty() != qty) {
				log.info("Total's qty was not synchronized. Update to " + qty);
				totalDao.update(qty);
			}
		} catch (BitcoinException e) {
			log.error(e.getMessage());
		}
		long doneTime = System.currentTimeMillis();
		log.info("sync total done at " + new Date() + ", total time is " + (doneTime - startTime));
	}
	
	@Scheduled(cron = "5 0 0 * * ?")
	public void tasks() {
		BitcoinJSONRPCClient client = getClient();
		if(client != null) {
			syncTotal(client);
			// 同步部分数据
			syncAddress(client);
			// 计算C文件
			try {
				cash(client);
			} catch (IOException | BitcoinException e) {
				log.error(e.getMessage());
			}
			// 计算H文件
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int day = c.get(Calendar.DAY_OF_MONTH);
			if(day == 4) {
				huihong();
			}
			// 分配address
			allocate(client);
			// 重启multichaind
			// TODO
		}
	}

}
