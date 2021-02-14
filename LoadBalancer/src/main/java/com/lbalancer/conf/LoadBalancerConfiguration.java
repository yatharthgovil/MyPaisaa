package com.lbalancer.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lbalancer.service.HeartBeatService;
import com.lbalancer.service.LoadAssignmentService;

@Component
public class LoadBalancerConfiguration {

	@Value("${app.heartbeat.endpoint}")
	private String[] addressHeartBeatProps;

	@Value("${app.message.endpoint}")
	private String[] addressMessageProps;
	private final List<String> addressesMessage = new ArrayList<String>();
	private final List<String> addressesHeartBeat = new ArrayList<String>();

	private final List<AtomicBoolean> heartBeatStatuses = new ArrayList<AtomicBoolean>();
	private final AtomicLong totalRequestCounter = new AtomicLong(0L);// requests counter for total requests
	private final List<AtomicLong> responseTimeRunningSums = new ArrayList<AtomicLong>();

	


	private final AtomicInteger requestBatchCounter = new AtomicInteger(0);// total requests in one batch before
																			// reassignment takes place
	private List<AtomicInteger> serviceProviderRequestCounters = new ArrayList<AtomicInteger>(); // total requests per
																									// serviceProvider
																									// before
																									// reassignment
																									// takesplace
	private List<AtomicInteger> requestsBatchPerServiceProvider = new ArrayList<AtomicInteger>(); // total requests to
	
	private final AtomicInteger maxRequestIndex =  new AtomicInteger(0);
	
	private final AtomicInteger requestsCurrentBatch = new AtomicInteger(0);
	
	private final  List<AtomicInteger> hashRing = new ArrayList<AtomicInteger>();

	public AtomicInteger getMaxRequestIndex() {
		return maxRequestIndex;
	}

	public AtomicInteger getRequestsCurrentBatch() {
		return requestsCurrentBatch;
	}
	


	public List<AtomicInteger> getHashRing() {
		return hashRing;
	}

	public List<String> getAddresses() {
		return addressesMessage;
	}

	public List<AtomicLong> getResponseTimeRunningSums() {
		return responseTimeRunningSums;
	}

	public List<AtomicBoolean> getHearBeatStatuses() {
		
		return heartBeatStatuses;
	}
	

	@Autowired
	private HeartBeatService heartBeatService;
	@Autowired
	private LoadAssignmentService loadAssignmentService;

	@PostConstruct
	private void init() {
		for (int i = 0; i < addressMessageProps.length; i++) {
			addressesMessage.add(addressMessageProps[i]);
			addressesHeartBeat.add(addressHeartBeatProps[i]);
			heartBeatStatuses.add(new AtomicBoolean(false));
			responseTimeRunningSums.add(new AtomicLong(0));
			serviceProviderRequestCounters.add(new AtomicInteger(0));
			requestsBatchPerServiceProvider.add(new AtomicInteger(0));
			hashRing.add(new AtomicInteger(0));
		}
	}

	public void loadHeartBeatStatuses() {
		heartBeatService.checkHeartBeat(addressesHeartBeat, heartBeatStatuses);
	}

	public void assignLoad() {
		loadAssignmentService.assignLoad(responseTimeRunningSums, serviceProviderRequestCounters, addressesMessage,
				requestBatchCounter, requestsBatchPerServiceProvider, heartBeatStatuses, requestsCurrentBatch, hashRing,
				maxRequestIndex);
	}

	public void incrementRequestCounter() {
		totalRequestCounter.incrementAndGet();
	}

	public int incrementRequestBatchCounter() {
		return requestBatchCounter.incrementAndGet();
	}

	public void incrementServiceProviderRequestCounter(int index) {
		serviceProviderRequestCounters.get(index).incrementAndGet();
	}
}
