package molab.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

import molab.Properties;
import molab.component.Data;
import molab.dao.AddressDao;
import molab.dao.TokenDao;
import molab.entity.Address;
import molab.util.RespUtil;
import molab.util.Status;

@Service
public class ApplyService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Properties properties;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private TokenDao tokenDao;

	public String apply(String token) {
		if (!checkToken(token)) {
			return RespUtil.errorResp(Status.Err.ILLEGAL_TOKEN.getCode(), Status.Err.ILLEGAL_TOKEN.getMsg());
		}
		// 查询可用address
		Example<Address> example = Example.of(new Address(Status.Common.CLOSE.getInt()));
		Pageable pageable = new PageRequest(0, 1);
		Page<Address> page = addressDao.findAll(example, pageable);
		List<Address> list = page.getContent();
		if (list != null && list.size() > 0) {
			Address address = list.get(0);
			addressDao.update(address.getId(), Status.Common.OPEN.getInt());
			// 返回结果
			Data data = new Data();
			data.setAddress(address.getAddress());
			return RespUtil.successResp(data);
		} else {
			BitcoinJSONRPCClient client = BitcoinJSONRPCClient.getInstance(null, null, properties.getRpcurl());
			if (client != null) {
				try {
					String address = client.getNewAddress();
					if (address != null) {
						// 新地址写入Address表
						Address addr = new Address(address, Status.Common.OPEN.getInt());
						addressDao.save(addr);
						// 返回结果
						Data data = new Data();
						data.setAddress(address);
						return RespUtil.successResp(data);
					}
				} catch (BitcoinException e) {
					log.error(e.getMessage());
				}
			}
		}
		return RespUtil.errorResp(Status.Err.SYSTEM_ERROR.getCode(), Status.Err.SYSTEM_ERROR.getMsg());
	}

	private boolean checkToken(String token) {
		return tokenDao.findByTokenAndState(token, Status.Common.OPEN.getInt()) != null;
	}

}
