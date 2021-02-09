package com.mypaisaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mypaisaa.consts.URIs;
import com.mypaisaa.dto.MessageDTO;
import com.mypaisaa.dto.ResponseDTO;
import com.mypaisaa.service.MessageService;



@RestController
@RequestMapping(path = {URIs.rootUri}, produces = "application/json")
public class MessageController {

	@Autowired
	public MessageService messageService;
	
	@GetMapping("/heartbeat")
	public ResponseEntity<String> heartBeat() {
		 HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-type", "appliocation/json");;
		return new ResponseEntity<>("{}",headers,HttpStatus.OK);
	}
	
	@PostMapping("/message/send")
	public ResponseDTO sendMesage(@RequestBody MessageDTO message) {
				
		return messageService.writeMessage(message);
	}
	
	
}
