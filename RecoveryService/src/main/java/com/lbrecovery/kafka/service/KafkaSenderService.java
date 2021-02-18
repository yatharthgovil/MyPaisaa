package com.lbrecovery.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbrecovery.dto.MessageDTO;

@Service
public class KafkaSenderService {

	@Autowired
	public KafkaTemplate<String, String> kafkaTemplate;
	private static Logger logger = LoggerFactory.getLogger(KafkaSenderService.class);
	
	@Autowired
    private ObjectMapper objectMapper;

	public void sendMessage(MessageDTO message, String topicName) {
		// kafkaTemplate.send(topicName, message);
		/*
		 * ListenableFuture<SendResult<String, MessageDTO>> future =
		 * kafkaTemplate.send(topicName, message);
		 * 
		 * future.addCallback(new ListenableFutureCallback<SendResult<String,
		 * MessageDTO>>() {
		 * 
		 * @Override public void onSuccess(SendResult<String, MessageDTO> result) {
		 * 
		 * LOG.info("Message [{}] delivered with offset {}", message,
		 * result.getRecordMetadata().offset());
		 * 
		 * 
		 * logger.info("success" + result.getRecordMetadata().timestamp()); }
		 * 
		 * @Override public void onFailure(Throwable ex) {
		 * logger.error("Unable to deliver message [{}]. {}", message, ex); } }); }
		 * 
		 */

		String messageJson = null;
		try {
			 messageJson = objectMapper.writeValueAsString(message);
		
		ListenableFuture<SendResult<String, String>> future1 = kafkaTemplate.send(topicName, messageJson);

		future1.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			@Override
			public void onSuccess(SendResult<String, String> result) {
				
				logger.info("success" + message.getId());
				message.setStatus("P");
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.error("Unable to deliver message [{}]. {}", message, ex);
				message.setStatus("F");
			}
		});
		
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("error in serializing boject",e);
		}
	}

}
