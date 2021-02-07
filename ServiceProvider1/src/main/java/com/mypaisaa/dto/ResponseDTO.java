package com.mypaisaa.dto;

import java.time.LocalDateTime;

public class ResponseDTO {

	private String result;
	private LocalDateTime timestamp;
	public ResponseDTO() {
		
	}
	public ResponseDTO(String result, LocalDateTime timestamp) {
		this .result = result;
		this.timestamp = timestamp;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
