package com.online.taxi.driver.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Value("${server.port}")
	String port;
	
	@RequestMapping("/hello")
	public String hello() {
		return "api-driver-hello:"+port;
	}
	
	@RequestMapping("/admin")
	public String admin() {
		return "api-driver-admin:"+port;
	}
	
	@RequestMapping("/token")
	public String cookie(HttpServletRequest req) {
		String token = req.getHeader("token");
		System.out.println("token:"+token);
		
		return "api-driver-token:"+token;
	}
	
}
