package com.simpleApplications.audioRecorder.handlers;

import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import com.simpleApplications.audioRecorder.utils.ResourceReader;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nico on 10.10.2015.
 */
public class IndexHandler implements IRequestHandler, ResourceReader {

    protected final String indexPageContent;

    public IndexHandler() {
        final InputStream fileStream = this.getResourceAsStream("web/index.html");
        final StringBuilder builder = new StringBuilder();

        try {
            int ch;

            while((ch = fileStream.read()) != -1){
                builder.append((char)ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.indexPageContent = builder.toString();
        System.out.println("Test");
    }

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/html");

        response.end(this.indexPageContent);
    }

    @Override
    public String getRoute() {
        return "/";
    }
}
