package com.simpleApplications.audioRecorder.handlers;

import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import com.simpleApplications.audioRecorder.utils.ResourceReader;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

/**
 * @author Nico Moehring
 */
public class StaticHandler implements IRequestHandler, ResourceReader {
    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", this.getHeaderContentType(routingContext.normalisedPath()));

        try {
            response.end(
                    Buffer.buffer(this.getFileContentAsByteArray("web" + routingContext.normalisedPath()))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRoute() {
        return "/static/*";
    }

    protected String getHeaderContentType(final String file) {
        final String[] split = file.split("\\.");
        final String fileType = split[split.length - 1];

        switch (fileType) {
            case "woff":
                return "application/font-woff";
            case "ttf":
                return "application/font-sfnt";
            case "eot":
                return "application/vnd.ms-fontobject";
            case "svg":
                return "image/svg+xml";
            default:
                return "";
        }
    }
}
