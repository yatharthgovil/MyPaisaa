package com.lbalancer.LoadBalancer;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableScheduling
@ComponentScan("com.lbalancer")
public class LoadBalancerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadBalancerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public ThreadPoolTaskExecutor poolTaskExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(10);
	    executor.setMaxPoolSize(50);
	    executor.setQueueCapacity(9000);
	    executor.setThreadNamePrefix("HeartBeatThread");
	    executor.initialize();
	    return executor;
	  }

}
