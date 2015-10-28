package com.simpleApplications.audioRecorder.handlers.interfaces;

import com.simpleApplications.audioRecorder.exceptions.ValidationException;

/**
 * @author Nico Moehring
 */
@FunctionalInterface
public interface IRequestHandlerConsumer<T> {
    void accept(T t) throws ValidationException;
}
