package molab.web;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import molab.Properties;
import molab.component.Data;
import molab.service.DownloadService;
import molab.util.Status;

@RestController
public class DownloadWeb {

	@Autowired
	private Properties properties;

	@Autowired
	private DownloadService service;

	// 下载C和H文件
	private final ResourceLoader resourceLoader;

	@Autowired
	public DownloadWeb(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@RequestMapping(value = "/c")
	@ResponseBody
	public ResponseEntity<?> c(@RequestParam String token, @RequestBody Data data) {
		String c = service.c(token, data);
		String filename = data.getDay();
		if (!Status.Err.SUCCESS.getMsg().equals(c)) {
			filename = c;
		}
		filename += ".txt";
		return ResponseEntity
				.ok(resourceLoader.getResource("file:" + Paths.get(properties.getCpath(), filename).toString()));
	}

	@RequestMapping(value = "/h")
	@ResponseBody
	public ResponseEntity<?> h(@RequestParam String token, @RequestBody Data data) {
		String h = service.h(token, data);
		String filename = data.getMonth();
		if (!Status.Err.SUCCESS.getMsg().equals(h)) {
			filename = h;
		}
		filename += ".txt";
		return ResponseEntity
				.ok(resourceLoader.getResource("file:" + Paths.get(properties.getHpath(), filename).toString()));
	}

}
