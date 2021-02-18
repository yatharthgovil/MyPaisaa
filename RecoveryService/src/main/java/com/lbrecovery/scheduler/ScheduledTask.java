package com.lbrecovery.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lbrecovery.service.MessageRetryService;


@Component
public class ScheduledTask {

	@Autowired
	private MessageRetryService messageRetryService;
    private static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
	
	@Scheduled(fixedRate = 60000,initialDelay = 1000)
	public void retry() {
		messageRetryService.retryMessages();
	}

}
