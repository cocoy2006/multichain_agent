package molab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import molab.component.Data;
import molab.service.BalanceService;

@RestController
public class BalanceWeb {
	
	@Autowired
	private BalanceService service;
	
	@ResponseBody
	@RequestMapping(value = "/balance")
	public String balance(@RequestParam String token, @RequestBody Data data) {
		return service.balance(token, data);
	}
	
}
