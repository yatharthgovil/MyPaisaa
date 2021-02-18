package com.lbrecovery.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lbrecovery.domain.Message;
import com.lbrecovery.dto.MessageDTO;
import com.lbrecovery.kafka.service.KafkaSenderService;
import com.lbrecovery.restclient.RetryClient;

@Service
public class MessageRetryService {

	@Autowired
	private RecoveryDbService recoveryDbService;

	@Autowired
	private RetryClient retryClient;

	@Autowired
	private KafkaSenderService kafkaSenderSrvice;

	@Value("${loadbalancer.status..endpoint}")
	private String loadBalancerStatusUrl;

	public void retryMessages() {

		List<Message> messages = recoveryDbService.getFailedMessages();
		List<Message> retriedMessages = new ArrayList<Message>();

		if (retryClient.checkStatus(loadBalancerStatusUrl)) {
			for (Message message : messages) {
				MessageDTO messageDTO = new MessageDTO();
				BeanUtils.copyProperties(message, messageDTO);
				kafkaSenderSrvice.sendMessage(messageDTO, "message_topic_lr_lb");
				BeanUtils.copyProperties(messageDTO, message);
				message.setStatus("P");
				message.setTriedAt(LocalDateTime.now());
				message.setUpdatedAt(LocalDateTime.now());
				retriedMessages.add(message);
			}
			recoveryDbService.saveMessages(retriedMessages);

		}

	}

}
