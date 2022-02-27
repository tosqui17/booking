package com.tosqui.app.exception;

public class CustomersInTroubleException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String message;
    


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


public CustomersInTroubleException(String message) {
    this.message=message;
}
}
