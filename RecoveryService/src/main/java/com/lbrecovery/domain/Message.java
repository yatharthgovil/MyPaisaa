package com.lbrecovery.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Message {
	
	@Id
	private BigInteger id;
	private String message;
	private String number;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String status;
	private LocalDateTime triedAt;

	public Message() {
		
	}
	
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getTriedAt() {
		return triedAt;
	}

	public void setTriedAt(LocalDateTime triedAt) {
		this.triedAt = triedAt;
	}

}
