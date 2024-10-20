package com.oksmart.kmcontrol.exception;

public class ServiceException extends RuntimeException {
    private String errorMessage;

    public ServiceException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
