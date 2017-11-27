package molab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import molab.component.Data;
import molab.dao.AddressDao;
import molab.dao.TokenDao;
import molab.dao.TotalDao;
import molab.entity.Address;
import molab.util.Molab;
import molab.util.RespUtil;
import molab.util.Status;

@Service
public class BalanceService {

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private TokenDao tokenDao;

	@Autowired
	private TotalDao totalDao;

	// 查询address对应的huihong数量
	public String balance(String token, Data req) {
		if (!checkToken(token)) {
			return RespUtil.errorResp(Status.Err.ILLEGAL_TOKEN.getCode(), Status.Err.ILLEGAL_TOKEN.getMsg());
		}
		if (req == null || req.getAddress() == null || !checkAddress(req.getAddress())) {
			return RespUtil.errorResp(Status.Err.ILLEGAL_ADDRESS.getCode(), Status.Err.ILLEGAL_ADDRESS.getMsg());
		}
		Data data = new Data();
		Address address = addressDao.findByAddress(req.getAddress());
		data.setQty(Molab.rescale(address.getQty()));
		data.setTotal(totalDao.findOne(1).getQty());
		return RespUtil.successResp(data);
	}

	private boolean checkAddress(String address) {
		return addressDao.findByAddressAndState(address, Status.Common.OPEN.getInt()) != null;
	}

	private boolean checkToken(String token) {
		return tokenDao.findByTokenAndState(token, Status.Common.OPEN.getInt()) != null;
	}

}
