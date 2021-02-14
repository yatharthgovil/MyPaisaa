package com.lbalancer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lbalancer.dto.MessageDTO;
import com.lbalancer.dto.ResponseDTO;
import com.lbalancer.service.RequestHandlerService;

@RestController
@RequestMapping(path = "/loadbalancer", produces = "application/json")
public class LoadBalancerController {

	@Autowired
	private RequestHandlerService requestHandlerService;
	@PostMapping("/message/send")
	public ResponseDTO sendMesage(@RequestBody MessageDTO message) {
		
		return requestHandlerService.handleRequest(message);
	
	}
}
