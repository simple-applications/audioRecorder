package com.simpleApplications.audioRecorder.exceptions;

/**
 * @author Nico Moehring
 */
public class NoDataGivenException extends HttpException {
    public NoDataGivenException() {
        super(400);
    }
}
