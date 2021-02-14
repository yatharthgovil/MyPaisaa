package com.lbalancer.restclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lbalancer.dto.MessageDTO;
import com.lbalancer.dto.ResponseDTO;
import com.lbalancer.exceptions.MessageNotSentException;

@Component
public class LoadRequestRestClient {

	@Autowired
	private RestTemplate restTemplate;
	private Logger logger = LoggerFactory.getLogger(LoadRequestRestClient.class);

	public ResponseDTO sendMessage(String url, MessageDTO messageDTO) throws Exception {

		logger.info("url------------" + url);

		HttpEntity<MessageDTO> request = new HttpEntity<>(messageDTO);
		ResponseEntity<ResponseDTO> response = null;

		try {
			long responseTimeStart = System.currentTimeMillis();

			response = restTemplate.postForEntity(url, request, ResponseDTO.class);
			long responseTimeEnd = System.currentTimeMillis();
			if(!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new MessageNotSentException("message to service at address could not be sent. Url----"+url);
			}
			long responseTime = responseTimeEnd - responseTimeStart;
			response.getBody().setResponseTime( responseTime == 0?1:responseTime);
		} catch (RestClientException e) {
			logger.error("error in sending message for address" + url, e);
		}
		

		return response.getBody();

	}

}
