package com.simpleApplications.audioRecorder.handlers;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Nico Moehring
 */
public class RecordingHandler extends AbstractRequestHandler {

    public RecordingHandler() {
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
