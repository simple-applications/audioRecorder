package com.simpleApplications.audioRecorder.handlers;

import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nico Moehring
 */
public abstract class AbstractRouteHandler implements IRequestHandler {
    protected List<String> routes = new ArrayList<>();

    @Override
    public List<String> getRoutes() {
        return this.routes;
    }
}
