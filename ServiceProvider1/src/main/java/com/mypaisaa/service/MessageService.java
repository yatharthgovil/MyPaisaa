package com.mypaisaa.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mypaisaa.dto.MessageDTO;
import com.mypaisaa.dto.ResponseDTO;
import com.mypaisaa.file.FileWriter;

@Service
public class MessageService {

	@Autowired
	private FileWriter fileWriter;
    private static Logger logger = LoggerFactory.getLogger(MessageService.class);

	public ResponseDTO writeMessage(MessageDTO message) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			logger.error("error occured"+e.getMessage());

		}
		boolean result = fileWriter.writeToFile(message);
		String respResult;
		if(result) {
			respResult = "success";
		}
		else {
			respResult = "failed";
		}
		ResponseDTO responseDTO = new ResponseDTO(respResult,LocalDateTime.now());
		return responseDTO;
	
	}
}
