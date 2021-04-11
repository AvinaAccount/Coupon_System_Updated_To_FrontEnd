package com.Avinadav.couponsystem.rest.models;

/**
 * Class that describing the respawn message to the client in JSON.
 */

public class ErrorResponse {
    private final String message;
    private final long timestamp;

    private ErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ErrorResponse ofNow(String msg) {
        return new ErrorResponse(msg, System.currentTimeMillis());
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
