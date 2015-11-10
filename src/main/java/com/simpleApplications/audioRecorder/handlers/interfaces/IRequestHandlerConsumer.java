package com.simpleApplications.audioRecorder.handlers.interfaces;

import com.simpleApplications.audioRecorder.exceptions.EntityNotFoundException;
import com.simpleApplications.audioRecorder.exceptions.HttpException;
import com.simpleApplications.audioRecorder.exceptions.NoDataGivenException;
import com.simpleApplications.audioRecorder.exceptions.ValidationException;

/**
 * @author Nico Moehring
 */
@FunctionalInterface
public interface IRequestHandlerConsumer<T> {
    void accept(T t) throws HttpException;
}
