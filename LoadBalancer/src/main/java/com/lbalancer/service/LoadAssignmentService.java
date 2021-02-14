package com.lbalancer.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoadAssignmentService {

	private static Logger logger = LoggerFactory.getLogger(LoadAssignmentService.class);

	public void assignLoad(List<AtomicLong> responseTimeRunningSums,
			List<AtomicInteger> serviceProviderRequestCounters, List<String> addresses,
			AtomicInteger requestBatchCounter, List<AtomicInteger> requestsBatchPerServiceProvider,
			List<AtomicBoolean> heartBeatStatuses, AtomicInteger requestsCurrentBatch, List<AtomicInteger> hashingRing,
			AtomicInteger maxServiceProviderIndex) {

		int responseTimeAvg[] = new int[addresses.size()];
		int workingNodes[] = new int[addresses.size()];
		int j=0;
		for(int i=0;i<addresses.size();i++) {
			if(heartBeatStatuses.get(i).get() == true) {
				workingNodes[j] = i;
				j++;
			} else {
				requestsBatchPerServiceProvider.get(i).set(0);
			}
		}
		for(int i=0;i<addresses.size();i++) {
			
			long runningSum = responseTimeRunningSums.get(i).get();
			responseTimeRunningSums.get(i).set(0);
			int serviceProviderRequests = serviceProviderRequestCounters.get(i).get();
			if(serviceProviderRequests>0)
			responseTimeAvg[i] = (int)runningSum/serviceProviderRequests;
			else 
				responseTimeAvg[i] = 0;
			serviceProviderRequestCounters.get(i).set(0);
		}		float proportions[] = new float[addresses.size()-1];
		float a = responseTimeAvg[workingNodes[0]];
		float prod = 1;
		int  k = 1;
		for(int i=1;i<j;i++) {
			int b = responseTimeAvg[workingNodes[k]] ;
			if(a>0&&b>0) {
				proportions[i-1] = b/a;
			}else {
				proportions[i-1] = 1.0f;
			}
				
			a = b;
			prod*=proportions[i-1];
			k++;
			//int requestsBatch = 100*proportions[i];
			//requestsBatchPerServiceProvider.get(i).set(newValue);
		}
		float denom = prod;
		float tempProd = prod;
		
			for(int i=0;i<j-1;i++) {
				tempProd = tempProd/proportions[i];
				denom += tempProd;
			}	
		
		
		tempProd = prod;
		
		int noRequests = requestBatchCounter.get();
		if(noRequests == 0)
			noRequests = 100;
		requestsCurrentBatch.set(noRequests);
		requestBatchCounter.set(0);
		int requestsBatchSize = Math.round(( noRequests* tempProd) /denom);
		requestsBatchPerServiceProvider.get(workingNodes[0]).set(requestsBatchSize);
		int maxSPIndex = workingNodes[0];
		logger.info("Requests assigned to service------"+addresses.get(workingNodes[0])+"Requests batch size"+requestsBatchSize);

		int max = requestsBatchSize;
		for(int i=0;i<j-1;i++) {
			tempProd = tempProd/proportions[i];
			requestsBatchSize = Math.round((noRequests * tempProd) /denom);
			if(requestsBatchSize > max) {
				max = requestsBatchSize;
				maxSPIndex = workingNodes[i+1];
			}
			logger.info("Requests assigned to service------"+addresses.get(workingNodes[i+1])+"Requests batch size"+requestsBatchSize);
			requestsBatchPerServiceProvider.get(workingNodes[i+1]).set(requestsBatchSize);
		}

		maxServiceProviderIndex.set(maxSPIndex);
		
		int no = requestsBatchPerServiceProvider.get(0).get();
		 hashingRing.get(0).set(no);

		for(int i=1;i<addresses.size();i++) {
			int temp = requestsBatchPerServiceProvider.get(i).get();
			if(temp !=0) {
				no+=temp;
				 hashingRing.get(i).set(no);
			} else {
				hashingRing.get(i).set(0);
			}
			 
		}
		
	}
	
}
