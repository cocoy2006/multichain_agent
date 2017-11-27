package molab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import molab.component.Data;
import molab.service.PublishService;

@RestController
public class PublishWeb {

	@Autowired
	private PublishService service;

	@ResponseBody
	@RequestMapping(value = "/publish")
	public String publish(@RequestParam String token, @RequestBody Data data) {
		return service.publish(token, data);
	}

}
