package com.lbalancer.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.lbalancer.restclient.HeartBeatRestClient;

@Service
public class HeartBeatService {

	@Autowired
	private HeartBeatRestClient heartBeatRestClient;

	@Autowired
	private ThreadPoolTaskExecutor pooltaskExecutor;

	private static Logger logger = LoggerFactory.getLogger(HeartBeatService.class);

	public void checkHeartBeat(List<String> addresses, List<AtomicBoolean> heartBeatStatuses) {

		logger.info("size of endpoints-------" + addresses.size());

		try {
			for (int i = 0; i < addresses.size(); i++) {
				Runnable task = new HeartBeatTaskWrapper(new HeartBeatTask(addresses.get(i)), i, heartBeatStatuses);
				pooltaskExecutor.submit(task);
				logger.info("Active Threads--->" + pooltaskExecutor.getActiveCount());
			}
		} catch (Exception e) {
			logger.error("error in submitting task to threadpool", e);

		}

	}

	public class HeartBeatTaskWrapper implements Runnable {
		public HeartBeatTask heartBeatTask;
		public int index;
		public List<AtomicBoolean> heartBeatStatuses;

		public HeartBeatTaskWrapper(HeartBeatTask heartBeatTask, int index,
				
				List<AtomicBoolean> heartBeatStatuses) {
			this.heartBeatTask = heartBeatTask;
			this.index = index;
			this.heartBeatStatuses = heartBeatStatuses;
		}

		public void run() {
			boolean status = heartBeatTask.run();
			logger.info("Status of service---" + heartBeatTask.address + "---status" + status);
			heartBeatStatuses.get(index).set(status);
			;
		}
	}

	public class HeartBeatTask {

		public String address;

		public HeartBeatTask(String address) {
			this.address = address;
		}

		public boolean run() {
			return heartBeatRestClient.checkHeartBeat(address);
		}
	}
}
