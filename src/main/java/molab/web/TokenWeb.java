package molab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import molab.service.TokenService;

@RestController
public class TokenWeb {
	
	@Autowired
	private TokenService service;

	@ResponseBody
	@RequestMapping(value = "/token")
	public String token(@RequestParam String appsecret) {
		return service.token(appsecret);
	}
	
}
