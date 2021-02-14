package com.lbalancer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.lbalancer.conf.LoadBalancerConfiguration;
import com.lbalancer.dto.MessageDTO;
import com.lbalancer.dto.ResponseDTO;
import com.lbalancer.restclient.LoadRequestRestClient;

@Service
public class RequestHandlerService {
	
	@Autowired
	private LoadBalancerConfiguration loadBalancerConfiguration;
	
	@Autowired
	private ThreadPoolTaskScheduler poolTaskExecutor;
	
	@Autowired
	private LoadRequestRestClient loadRequestRestClient;
	private LoadAssignmentService loadAssignmentService;
	@Autowired
	private MessageQueueService messageQueueService;
	
	public ResponseDTO handleRequest(MessageDTO messageDTO) {
		
		loadBalancerConfiguration.incrementRequestCounter();
		int requestNoCurrentBatch = loadBalancerConfiguration.incrementRequestBatchCounter();
		if(requestNoCurrentBatch == 1000)
			loadBalancerConfiguration.assignLoad();

		int serviceProviderIndex = resolveServiceProviderIndex(requestNoCurrentBatch,
				loadBalancerConfiguration.getRequestsCurrentBatch(), loadBalancerConfiguration.getHashRing(),
				loadBalancerConfiguration.getMaxRequestIndex());
		List<String> addresses = loadBalancerConfiguration.getAddresses();
		if(loadBalancerConfiguration.getHearBeatStatuses().get(serviceProviderIndex).get()== false) {
			loadBalancerConfiguration.assignLoad();
			serviceProviderIndex = resolveServiceProviderIndex(requestNoCurrentBatch,
					loadBalancerConfiguration.getRequestsCurrentBatch(), loadBalancerConfiguration.getHashRing(),
					loadBalancerConfiguration.getMaxRequestIndex());
		}
	    
	    loadBalancerConfiguration.incrementServiceProviderRequestCounter(serviceProviderIndex);
		LoadRequestTaskWrapper loadRequestTaskWrapper = new LoadRequestTaskWrapper(
				new LoadRequestTask(addresses.get(serviceProviderIndex), messageDTO), serviceProviderIndex,
				loadBalancerConfiguration.getResponseTimeRunningSums());
		poolTaskExecutor.submit(loadRequestTaskWrapper);
		return new ResponseDTO("Processing",LocalDateTime.now());
	}
	
	private long hash(long x) {
	    x = ((x >>> 16) ^ x) * 0x45d9f3b;
	    x = ((x >>> 16) ^ x) * 0x45d9f3b;
	    x = (x >>> 16) ^ x;
	    return x;
	}
	
	
	private int resolveServiceProviderIndex(int rNo, AtomicInteger requestCurrentBatch, List<AtomicInteger> hashRing, AtomicInteger maxSPIndex) {
		int totalRequests = requestCurrentBatch.get();
		int serviceProviderHash = (int) (hash(rNo) % (totalRequests +1));
		int i = 0;
		for(i=0;i<hashRing.size();i++) {
			if(hashRing.get(i).get()>serviceProviderHash)
				return i;
		}
		
			return maxSPIndex.get();
	}
	
	public class LoadRequestTaskWrapper implements Runnable {
		public LoadRequestTask loadRequestTask;
		public int index;
		public List<AtomicLong> runningSums;

		public LoadRequestTaskWrapper(LoadRequestTask loadRequestTask, int index,	List<AtomicLong> runningSums) {
			this.loadRequestTask = loadRequestTask;
			this.index = index;
			this.runningSums = runningSums;
		}

		public void run() {
			try {
				ResponseDTO response = loadRequestTask.run();
				runningSums.get(index).addAndGet(response.getResponseTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				messageQueueService.send(loadRequestTask.messageDTO);
			}
		   
		}
	}

	public class LoadRequestTask {

		public String address;
		public MessageDTO messageDTO;

		public LoadRequestTask(String address,MessageDTO messageDTO) {
			this.address = address;
			this.messageDTO = messageDTO;
		}

		public ResponseDTO run() throws Exception{
			return loadRequestRestClient.sendMessage(address,messageDTO);
		}
	}

}
