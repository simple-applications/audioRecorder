package com.simpleApplications.audioRecorder.handlers;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.exceptions.ValidationException;
import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandlerConsumer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

/**
 * @author Nico Moehring
 */
public abstract class AbstractRequestHandler<T> implements IRequestHandler {

    protected Map<HttpMethod, IRequestHandlerConsumer<RoutingContext>> handledMethods = new HashMap<>();

    protected Validator validator;

    @Inject
    public AbstractRequestHandler(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        final HttpMethod requestMethod = routingContext.request().method();

        if (this.handledMethods.containsKey(requestMethod)) {
            try {
                this.handledMethods.get(requestMethod).accept(routingContext);
            } catch (ValidationException e) {
                this.handleValidationErrors(routingContext, e);
            }
        } else {
            routingContext.fail(405);
        }
    }

    protected void generateValidationException(Set<ConstraintViolation<T>> constraintViolations) throws ValidationException {
        final List<AbstractMap.SimpleEntry<String, String>> validationErrors = new ArrayList<>();

        constraintViolations.forEach(constraintViolation -> {
            validationErrors.add(new AbstractMap.SimpleEntry<>(
                    constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getMessage()
            ));
        });

        throw new ValidationException(validationErrors);
    }

    protected void handleValidationErrors(RoutingContext routingContext, ValidationException validationException) {
        final JsonObject result = new JsonObject();
        final JsonArray errors = new JsonArray();
        result.put("status", "error");

        validationException.getValidationErrors().forEach(validationError -> {
            JsonObject tmpError = new JsonObject();
            tmpError.put(validationError.getKey(), validationError.getValue());

            errors.add(tmpError);
        });

        result.put("errors", errors);

        routingContext.response().end(result.encode());
    }
}
