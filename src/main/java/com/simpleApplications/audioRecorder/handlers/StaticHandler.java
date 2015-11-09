package com.simpleApplications.audioRecorder.handlers;

import com.simpleApplications.audioRecorder.utils.ResourceReader;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

/**
 * @author Nico Moehring
 */
public class StaticHandler extends AbstractRouteHandler implements ResourceReader {

    public StaticHandler() {
        this.routes.add("/static/*");
    }

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
