package com.lbalancer.exceptions;

public class MessageNotSentException extends RuntimeException {
	
	public MessageNotSentException(String message) {
		super(message);
	}

}
