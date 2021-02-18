package com.lbalancer.dto;

import java.math.BigInteger;

public class MessageDTO {

	private BigInteger id;
	private String message;
	private String number;
	
	public MessageDTO(String no, String message) {
		this.number = no;
		this.message = message;
	}
	public MessageDTO() {
		
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
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
}
