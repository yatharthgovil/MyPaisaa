package com.lbalancer.restclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lbalancer.scheduler.ScheduledTask;

@Component
public class HeartBeatRestClient {

	@Autowired
	private RestTemplate restTemplate;
	private Logger logger = LoggerFactory.getLogger(HeartBeatRestClient.class);

	public boolean checkHeartBeat(String url) {

		//logger.info("url------------" + url);

		ResponseEntity<String> response = null;

		try {
			response = restTemplate.getForEntity(url, String.class);
		} catch (RestClientException e) {
			logger.error("error in checking heartbeeate for address" + url, e.getMessage());
		}

		return response != null ? response.getStatusCode().equals(HttpStatus.OK) : false;

	}

}
