package com.tosqui.app.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;
	


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public UserNotFoundException(String message) {
		this.message=message;
	}

}
