package com.simpleApplications.audioRecorder.handlers.interfaces;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by nico on 10.10.2015.
 */
public interface IRequestHandler extends Handler<RoutingContext> {
    public String getRoute();
}
