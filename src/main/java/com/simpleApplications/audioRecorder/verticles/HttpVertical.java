package com.simpleApplications.audioRecorder.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;

/**
 * @author Nico Moehring
 */
public class HttpVertical extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        this.startHttpServer();
    }

    private void startHttpServer() {

    }
}
