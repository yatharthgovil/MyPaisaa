package com.lbalancer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lbalancer.dto.MessageDTO;
import com.lbalancer.dto.ResponseDTO;
import com.lbalancer.kafka.service.KafkaSenderService;
import com.lbalancer.service.RequestHandlerService;

@RestController
@RequestMapping(path = "/loadbalancer", produces = "application/json")
public class LoadBalancerController {

	@Autowired
	private RequestHandlerService requestHandlerService;
	
	@Autowired
	private KafkaSenderService kafkaSenderService;
	
	@PostMapping("/message/send")
	public ResponseDTO sendMesage(@RequestBody MessageDTO message) {
		
		return requestHandlerService.handleRequest(message);
		//kafkaSenderService.sendMessage(message, "message_topic_test");
		//return new ResponseDTO("success");
	
	}
	
	@GetMapping("/status")
	public ResponseDTO checkSPStatus() {
		return requestHandlerService.checkStatus();
	}
}
