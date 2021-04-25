package com.lbalancer.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lbalancer.conf.LoadBalancerConfiguration;


@Component
public class ScheduledTask {

	@Autowired
	private LoadBalancerConfiguration loadConfiguration;
    private static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

	@Scheduled(fixedRate = 1000)
	public void checkHeartBeat() {

		loadConfiguration.loadHeartBeatStatuses();

	}
	
	@Scheduled(fixedRateString = "${scheduler.fixed.delay}",initialDelay = 1200)
	public void assignLoad() {

		loadConfiguration.assignLoad();

	}

}
