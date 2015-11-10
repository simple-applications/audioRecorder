package com.simpleApplications.audioRecorder.exceptions;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nico Moehring
 */
public class ValidationException extends HttpException {
    protected List<AbstractMap.SimpleEntry<String, String>> validationErrors = new ArrayList<>();

    public ValidationException(List<AbstractMap.SimpleEntry<String, String>> validationErrors) {
        super(422);

        this.validationErrors = validationErrors;

        final JsonObject result = new JsonObject();
        final JsonObject errors = new JsonObject();

        this.validationErrors.forEach(validationError -> {
            JsonArray errorArray = errors.getJsonArray(validationError.getKey());

            if (errorArray == null) {
                errorArray = new JsonArray();
            }

            errorArray.add(validationError.getValue());

            errors.put(validationError.getKey(), errorArray);
        });

        result.put("errors", errors);

        this.response = result.toString();
    }

    public List<AbstractMap.SimpleEntry<String, String>> getValidationErrors() {
        return validationErrors;
    }
}
