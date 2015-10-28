package com.simpleApplications.audioRecorder.handlers;

import com.simpleApplications.audioRecorder.model.Recording;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import javax.validation.Validator;

/**
 * @author Nico Moehring
 */
public class RecordingHandler extends AbstractRequestHandler<Recording> {

    public RecordingHandler(Validator validator) {
        super(validator);

        this.handledMethods.put(HttpMethod.GET, this::getRecordings);
        this.handledMethods.put(HttpMethod.DELETE, this::deleteRecording);
    }

    @Override
    public String getRoute() {
        return "/recordings";
    }

    protected void deleteRecording(RoutingContext routingContext) {

    }

    protected void getRecordings(RoutingContext routingContext) {

    }
}
