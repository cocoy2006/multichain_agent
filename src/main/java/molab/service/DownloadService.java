package molab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import molab.component.Data;
import molab.dao.TokenDao;
import molab.util.Status;

@Service
public class DownloadService {

	@Autowired
	private TokenDao tokenDao;

	public String c(String token, Data req) {
		if (!checkToken(token)) {
			return Status.Err.ILLEGAL_TOKEN.getMsg();
		}
		if (req == null || req.getDay() == null) {
			return Status.Err.ILLEGAL_PARAMETER.getMsg();
		}
		return Status.Err.SUCCESS.getMsg();
	}

	public String h(String token, Data req) {
		if (!checkToken(token)) {
			return Status.Err.ILLEGAL_TOKEN.getMsg();
		}
		if (req == null || req.getMonth() == null) {
			return Status.Err.ILLEGAL_PARAMETER.getMsg();
		}
		return Status.Err.SUCCESS.getMsg();
	}

	private boolean checkToken(String token) {
		return tokenDao.findByTokenAndState(token, Status.Common.OPEN.getInt()) != null;
	}

}
