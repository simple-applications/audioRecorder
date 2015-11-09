package com.simpleApplications.audioRecorder.handlers;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.exceptions.EntityNotFoundException;
import com.simpleApplications.audioRecorder.exceptions.NoDataGivenException;
import com.simpleApplications.audioRecorder.exceptions.ValidationException;
import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandlerConsumer;
import com.simpleApplications.audioRecorder.model.JsonObjectConverter;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author Nico Moehring
 */
public abstract class AbstractRequestHandler<T extends JsonObjectConverter> extends AbstractRouteHandler {

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
            } catch (NoDataGivenException e) {
                this.handleNoDataGivenException(routingContext);
            } catch (EntityNotFoundException e) {
                this.handleEntityNotFoundException(routingContext);
            }
        } else {
            routingContext.fail(405);
        }
    }

    protected T getEntityFromRequest(RoutingContext routingContext) throws NoDataGivenException {
        final T entity = this.getGenericInstance();

        try {
            JsonObject jsonObject = routingContext.getBodyAsJson();

            if (jsonObject == null || jsonObject.isEmpty()) {
                throw new NoDataGivenException();
            }

            entity.bindJson(jsonObject);
        } catch (DecodeException e) {
            throw new NoDataGivenException();
        }

        return entity;
    }

    protected void validateEntity(T entity) throws ValidationException {
        Set<ConstraintViolation<T>> violations = this.validator.validate(entity);

        if (!violations.isEmpty()) {
            this.generateValidationException(violations);
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
        final JsonObject errors = new JsonObject();

        validationException.getValidationErrors().forEach(validationError -> {
            JsonArray errorArray = errors.getJsonArray(validationError.getKey());

            if (errorArray == null) {
                errorArray = new JsonArray();
            }

            errorArray.add(validationError.getValue());

            errors.put(validationError.getKey(), errorArray);
        });

        result.put("errors", errors);

        routingContext
                .response()
                .setStatusCode(422)
                .end(result.encode());
    }

    protected void handleNoDataGivenException(RoutingContext routingContext) {
        routingContext
                .response()
                .setStatusCode(400)
                .end();
    }

    protected void handleEntityNotFoundException(RoutingContext routingContext) {
        routingContext
                .response()
                .setStatusCode(404)
                .end();
    }

    protected void sendEntityResponse(RoutingContext routingContext, T entity) {
        routingContext.response().end(entity.toJson().toString());
    }

    protected JsonArray generateJsonArray(List<T> entities) {
        JsonArray result = new JsonArray();

        entities.forEach(entity -> {
            result.add(entity.toJson());
        });

        return result;
    }

    private T getGenericInstance() {
        final ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        final Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];

        try  {
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
