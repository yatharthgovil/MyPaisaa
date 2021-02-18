package com.lbrecovery.restclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lbrecovery.dto.ResponseDTO;

@Component
public class RetryClient {

	@Autowired
	private RestTemplate restTemplate;
	private Logger logger = LoggerFactory.getLogger(RetryClient.class);

	public boolean checkStatus(String url) {

		// logger.info("url------------" + url);

		ResponseEntity<ResponseDTO> response = null;

		try {
			response = restTemplate.getForEntity(url, ResponseDTO.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				return response.getBody().getResult().equalsIgnoreCase("up");
			}
		} catch (RestClientException e) {
			logger.error("error in checking heartbeeate for address" + url, e.getMessage());
		}

		return false;
	}
	
	
}