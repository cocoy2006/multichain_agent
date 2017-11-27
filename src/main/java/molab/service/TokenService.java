package molab.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import molab.Properties;
import molab.component.Data;
import molab.dao.TokenDao;
import molab.entity.Token;
import molab.util.RespUtil;
import molab.util.Status;

@Service
public class TokenService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Properties properties;

	@Autowired
	private TokenDao tokenDao;

	private boolean checkToken(String token) {
		return tokenDao.findByTokenAndState(token, Status.Common.OPEN.getInt()) != null;
	}

	private void closeToken(Token token) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				log.info("Token(" + token.getToken() + ") will close in 24 hours.");
				tokenDao.update(token.getId(), Status.Common.CLOSE.getInt(), System.currentTimeMillis());
			}

		}, 86400000);
	}

	private void closeTokens(List<Token> tokenList) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				for (Token token : tokenList) {
					log.info("Token(" + token.getToken() + ") will close in one hour.");
					tokenDao.update(token.getId(), Status.Common.CLOSE.getInt(), System.currentTimeMillis());
				}
			}

		}, 3600000);
	}

	private String getToken(String appsecret) {
		return DigestUtils.sha1Hex(appsecret + System.currentTimeMillis());
	}

	public String token(String appsecret) {
		if (appsecret == null || !appsecret.equalsIgnoreCase(properties.getAppsecret())) {
			return RespUtil.errorResp(Status.Err.ILLEGAL_APPSECRET.getCode(), Status.Err.ILLEGAL_APPSECRET.getMsg());
		}
		try {
			String token = getToken(appsecret);
			while (checkToken(token)) {
				token = getToken(appsecret);
			}
			// 查询token记录
			Example<Token> example = Example.of(new Token(Status.Common.OPEN.getInt()));
			List<Token> tokenList = tokenDao.findAll(example);
			if (tokenList != null && tokenList.size() > 0) {
				// 有token记录，旧token计时作废
				closeTokens(tokenList);
			}
			// 插入新token
			Token newToken = tokenDao.save(new Token(token));
			closeToken(newToken);
			// 返回结果
			Data data = new Data();
			data.setToken(token);
			data.setExpires(86400);
			return RespUtil.successResp(data);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return RespUtil.errorResp(Status.Err.SYSTEM_ERROR.getCode(), Status.Err.SYSTEM_ERROR.getMsg());
	}

}
