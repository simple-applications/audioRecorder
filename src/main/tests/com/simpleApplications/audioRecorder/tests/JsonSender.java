package com.simpleApplications.audioRecorder.tests;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.ext.unit.TestContext;

/**
 * @author Nico Moehring
 */
public interface JsonSender {
    default void sendJsonRequest(TestContext context, HttpClientRequest request, String message, Handler<HttpClientResponse> responseHandler) {
        request.exceptionHandler(err -> context.fail(err.getMessage()));
        request.handler(responseHandler);
        request.putHeader("Content-Length", String.valueOf(message.length()));
        request.putHeader("Content-Type", "application/json");
        request.write(message);
        request.end();
    }
}
