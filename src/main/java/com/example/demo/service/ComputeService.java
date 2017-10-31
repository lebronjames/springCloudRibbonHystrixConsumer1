package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ComputeService {

	@Autowired
	RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod="addServiceFallback")
	public String add() {
		return restTemplate.getForEntity("http://COMPUTE-SERVICE/add?a=11&b=21", String.class)
				.getBody();
	}
	
	public String addServiceFallback() {
		return "ERROR";
	}
}
