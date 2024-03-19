package org.example.loanbusinessv3.util;

public class HandleError {
    private String message;
    private int statusCode;
    private String details;

    public HandleError(String message, int statusCode, String details) {
        this.message = message;
        this.statusCode = statusCode;
        this.details = details;
    }

    public HandleError() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
