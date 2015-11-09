package com.simpleApplications.audioRecorder.handlers.interfaces;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * @author Nico Moehring
 */
public interface IRequestHandler extends Handler<RoutingContext> {
    List<String> getRoutes();
}
