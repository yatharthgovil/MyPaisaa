package com.lbrecovery.kafka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbrecovery.dto.MessageDTO;
import com.lbrecovery.service.RecoveryDbService;

@Component
public class MessageListener {

	private static Logger logger = LoggerFactory.getLogger(MessageListener.class);

	@Autowired
	private RecoveryDbService recoveryDbService;

	@Autowired
	private ObjectMapper objetMapper;

	@KafkaListener(topics = "message-topic", groupId = "recoverygroup1", containerFactory = "kafkaListenerContainerFactory")
	void listenerMessageTopic(MessageDTO message) {
		// LOG.info("CustomUserListener [{}]", user);
		logger.info("Message Recieved" + message.getMessage() + "-----" + message.getNumber());

	}

	@KafkaListener(topics = "message_topic_lb_lr", groupId = "recoverygroup1")
	void listenerMessageTopicTest(String message) {
		// LOG.info("CustomUserListener [{}]", user);
		logger.info("Message Recieved" + message);
		try {
			MessageDTO messageDTO = objetMapper.readValue(message, MessageDTO.class);
			recoveryDbService.saveMessage(messageDTO);
		} catch (JsonProcessingException e) {
			logger.error("error in parsing messaeg json", e);
		}

	}
}
