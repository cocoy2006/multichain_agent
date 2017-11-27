package molab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import molab.service.ApplyService;

@RestController
public class ApplyWeb {
	
	@Autowired
	private ApplyService service;

	@ResponseBody
	@RequestMapping(value = "/apply")
	public String apply(@RequestParam String token) {
		return service.apply(token);
	}
	
}
