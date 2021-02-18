package com.lbrecovery.kafka.conf;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	
	@Bean
	public NewTopic messageTopic() {
	    return TopicBuilder.name("message_topic_lb_lr").build();
	  }

	@Bean
	public NewTopic messageTopicTest() {
	    return TopicBuilder.name("message_topic-test").build();
	  }
}
