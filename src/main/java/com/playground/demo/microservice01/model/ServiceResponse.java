package com.playground.demo.microservice01.model;

public class ServiceResponse {

    private int status;

    private String message;

    public ServiceResponse() {
        super();
    }

    public ServiceResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
