package com.lbrecovery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbrecovery.domain.Message;
import com.lbrecovery.dto.MessageDTO;
import com.lbrecovery.repository.MessageRepository;

@Service
public class RecoveryDbService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	private static Logger logger = LoggerFactory.getLogger(RecoveryDbService.class);

	public boolean saveMessage(MessageDTO messageDTO) {
		Message message = new Message();
		try {
			BeanUtils.copyProperties(messageDTO, message);
			
			message.setStatus("F");//failed
			message.setStatus("F");//failed
			logger.info("persisting message----"+message.getNumber()+" ------> "+message.getMessage());

			if(message.getId()!=null) {
				message.setUpdatedAt(LocalDateTime.now());
			}else {
				message.setCreatedAt(LocalDateTime.now());
			}
			return messageRepository.save(message) != null;
		}
		catch(Exception e ) {
			logger.error("error in persisting message to db."+messageDTO.toString(),e);
		}
		return false;
		
	}
	
	public boolean saveMessages(List<Message> messages) {
		try {
			 messageRepository.saveAll(messages);
			 return true;

		} catch(Exception e) {
			logger.error("error in persisting message to db.",e);

		}
		return false;
	}

	public List<Message> getFailedMessages() {
		 return messageRepository.findAllByStatusOrderByCreatedAt("F");
	}
}

