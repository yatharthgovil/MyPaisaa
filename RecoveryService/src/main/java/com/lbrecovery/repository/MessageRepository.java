package com.lbrecovery.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lbrecovery.domain.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message,Long> {
	
	
	public List<Message> findAllByStatusOrderByCreatedAt(String status);
	

}
