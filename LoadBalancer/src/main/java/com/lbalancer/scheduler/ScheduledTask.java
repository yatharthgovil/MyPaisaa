package com.lbalancer.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lbalancer.conf.LoadConfiguration;


@Component
public class ScheduledTask {

	@Autowired
	private LoadConfiguration loadConfiguration;
    private static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

	@Scheduled(fixedRate = 1000)
	public void checkHeartBeat() {

		logger.info("job started------------");
		loadConfiguration.loadHeartBeatStatuses();

	}

}
