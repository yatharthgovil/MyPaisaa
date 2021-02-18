package com.lbalancer.kafka.listener;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbalancer.dto.MessageDTO;
import com.lbalancer.service.RequestHandlerService;

@Component
public class MessageListener {

	private static Logger logger = LoggerFactory.getLogger(MessageListener.class);

	@Autowired
	private ObjectMapper objetMapper;
	
	@Autowired
	private RequestHandlerService requestHandlerService;
	
	@KafkaListener(topics = "message_topic_lr_lb", groupId = "loadbalancergroup1")
	void listenerMessageTopicTest(String message) {
		// LOG.info("CustomUserListener [{}]", user);
		logger.info("Message Recieved" + message);
		try {
			MessageDTO messageDTO = objetMapper.readValue(message, MessageDTO.class);
			requestHandlerService.handleRequest(messageDTO);
		} catch (JsonProcessingException e) {
			logger.error("error in parsing messaeg json", e);
		}

	}
}
