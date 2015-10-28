package com.simpleApplications.audioRecorder.exceptions;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nico Moehring
 */
public class ValidationException extends Exception {
    protected List<AbstractMap.SimpleEntry<String, String>> validationErrors = new ArrayList<>();

    public ValidationException(List<AbstractMap.SimpleEntry<String, String>> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<AbstractMap.SimpleEntry<String, String>> getValidationErrors() {
        return validationErrors;
    }
}
