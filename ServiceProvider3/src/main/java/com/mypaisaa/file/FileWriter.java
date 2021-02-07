package com.mypaisaa.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypaisaa.dto.MessageDTO;

@Component
public class FileWriter {
	
	@Value("${file.path}")
	public String filePath;
	@Autowired
    private ObjectMapper objectMapper;
	
    private static Logger logger = LoggerFactory.getLogger(FileWriter.class);

	
	public boolean writeToFile(MessageDTO message) {

		logger.info("writing mesage for number---->"+message.getNumber());;
		logger.info("Message----------->"+message.getMessage());;
		try {
            String msgJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
            Path path = Paths.get(filePath);
            msgJson = msgJson+"\r\n";
            byte[] strToBytes = msgJson.getBytes();
           

            Files.write(path, strToBytes,StandardOpenOption.CREATE,StandardOpenOption.APPEND);    
        
		return true;
		} catch (JsonProcessingException e) {
            logger.error("failed conversion: message to Json",e);;
        }
		catch(IOException ex) {
			logger.error("could not write to file",ex);
		}
		return false;
	}

}
