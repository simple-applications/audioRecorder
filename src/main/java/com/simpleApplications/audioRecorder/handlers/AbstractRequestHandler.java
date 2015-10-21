package com.simpleApplications.audioRecorder.handlers;

import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Nico Moehring
 */
public abstract class AbstractRequestHandler implements IRequestHandler {

    protected Map<HttpMethod, Consumer<RoutingContext>> handledMethods = new HashMap<>();

    @Override
    public void handle(RoutingContext routingContext) {
        final HttpMethod requestMethod = routingContext.request().method();

        if (this.handledMethods.containsKey(requestMethod)) {
            this.handledMethods.get(requestMethod).accept(routingContext);
        } else {
            routingContext.fail(405);
        }
    }
}
