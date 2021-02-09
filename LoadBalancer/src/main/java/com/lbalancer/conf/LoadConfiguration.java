package com.lbalancer.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lbalancer.service.HeartBeatService;

@Component
public class LoadConfiguration {

	@Value("${app.endpoint}")
	private String[] addressprops ;
	private final List<String> addresses = new ArrayList<String>();
	
	@Autowired
	public HeartBeatService heartBeatService;
	
	public final ConcurrentMap<Integer,Boolean> heartBeatStatuses = new ConcurrentHashMap<Integer,Boolean>();
	
	@PostConstruct	
	public void init() {
		for(int i=0;i<addressprops.length;i++) {
			addresses.add(addressprops[i]);
			heartBeatStatuses.put(i, false);
		}
	}
	
	public void loadHeartBeatStatuses() {
		heartBeatService.checkHeartBeat(addresses, heartBeatStatuses);		
	}
	
	
}
