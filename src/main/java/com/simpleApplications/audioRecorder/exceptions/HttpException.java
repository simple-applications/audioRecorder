package com.simpleApplications.audioRecorder.exceptions;

/**
 * @author Nico Moehring
 */
public abstract class HttpException extends Exception {
    protected int statusCode;

    protected String response = "";

    public HttpException(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
