package com.simpleApplications.audioRecorder.handlers.interfaces;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Nico Moehring
 */
public interface IRequestHandler extends Handler<RoutingContext> {
    public String getRoute();
}
