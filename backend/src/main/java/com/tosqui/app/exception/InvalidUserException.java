package com.tosqui.app.exception;

public class InvalidUserException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String message;
    


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


public InvalidUserException(String message) {
    this.message=message;
}
}
