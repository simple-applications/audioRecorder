package com.simpleApplications.audioRecorder.exceptions;

/**
 * @author Nico Moehring
 */
public class EntityNotFoundException extends HttpException {
    public EntityNotFoundException() {
        super(404);
    }
}
