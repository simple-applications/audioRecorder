 package com.simpleApplications.audioRecorder.verticles;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.actions.DatabaseActionFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

 /**
 * @author Nico Moehring
 */
public class HttpVertical extends AbstractVerticle {

    protected DatabaseActionFactory actionFactory;

    @Inject
    public HttpVertical(DatabaseActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public void start() throws Exception {

    }
}
