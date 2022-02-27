package com.tosqui.app.ui.model.response;

public class JSONResponse {
    private String message;

    public JSONResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}