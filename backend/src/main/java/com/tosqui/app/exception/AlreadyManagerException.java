package com.tosqui.app.exception;

public class AlreadyManagerException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String message;
    


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


public AlreadyManagerException(String message) {
    this.message=message;
}
}
