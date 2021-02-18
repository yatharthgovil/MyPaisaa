package com.lbalancer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbalancer.dto.MessageDTO;
import com.lbalancer.kafka.service.KafkaSenderService;

@Service
public class MessageQueueService {

	@Autowired
	private KafkaSenderService kafkaSenderService;
	public void send(MessageDTO messageDTO, String topic) {
		kafkaSenderService.sendMessage(messageDTO, topic);
	}
}
