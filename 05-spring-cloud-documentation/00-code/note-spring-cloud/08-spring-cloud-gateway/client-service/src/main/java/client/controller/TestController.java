package client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
public class TestController {
	
	@Autowired
	private RestTemplate rest;
	
	@RequestMapping("/test")
	public String test(HttpServletRequest request) {
		System.out.println("----------------success access test method!----------------");
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			System.out.println(key + ": " + request.getHeader(key));
		}
		return "success access test method!!";
	}
	
	@RequestMapping("/accessProvider")
	public String accessProvider(HttpServletRequest request) {
		String result = rest.getForObject("http://provider-service/provider/test", String.class);
		return result;
	}
	
}
