package com.simpleApplications.audioRecorder.handlers;

import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import com.simpleApplications.audioRecorder.utils.ResourceReader;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

/**
 * @author Nico Moehring
 */
public class IndexHandler implements IRequestHandler, ResourceReader {

    protected final static String CSS_PLACEHOLDER = "###CSS_CONTENT###";

    protected final String indexPageContent;

    public IndexHandler() {
        StringBuilder indexContentBuilder = new StringBuilder();

        try {
            indexContentBuilder.append(this.getFileContentAsString("web/index.html"));
            final StringBuilder fontsContentBuilder = new StringBuilder(this.getFileContentAsString("web/fonts.css"));
            final int cssStart = indexContentBuilder.indexOf(IndexHandler.CSS_PLACEHOLDER);

            fontsContentBuilder.append(this.getFileContentAsString("web/styles.css"));

            indexContentBuilder.delete(cssStart, cssStart + IndexHandler.CSS_PLACEHOLDER.length());
            indexContentBuilder.insert(cssStart, fontsContentBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.indexPageContent = indexContentBuilder.toString();
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
