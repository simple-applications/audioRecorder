package com.simpleApplications.audioRecorder.handlers;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * @author Nico Moehring
 */
public class RecordingProjectHandler extends AbstractRequestHandler {

    protected IRecordingProjectDao recordingProjectDao;

    @Inject
    public RecordingProjectHandler(IRecordingProjectDao recordingProjectDao) {
        this.recordingProjectDao = recordingProjectDao;

        this.handledMethods.put(HttpMethod.GET, this::getRecordingProjects);
        this.handledMethods.put(HttpMethod.PUT, this::createRecordingProject);
        this.handledMethods.put(HttpMethod.POST, this::saveRecordingProject);
        this.handledMethods.put(HttpMethod.DELETE, this::deleteRecordingProject);
    }

    @Override
    public String getRoute() {
        return "/recordingProjects";
    }

    protected void deleteRecordingProject(RoutingContext routingContext) {

    }

    protected void saveRecordingProject(RoutingContext routingContext) {

    }

    protected void createRecordingProject(RoutingContext routingContext) {

    }

    protected void getRecordingProjects(RoutingContext routingContext) {
        Vertx.currentContext().executeBlocking((Future<List<RecordingProject>> objectFuture) -> {
            objectFuture.complete(this.recordingProjectDao.getAll());
        }, result -> {
            if (result.succeeded()) {
                List<RecordingProject> recordingProjectList = result.result();
                JsonArray jsonResult = new JsonArray();

                for (RecordingProject tmpProject : recordingProjectList) {
                    jsonResult.add(tmpProject.toJson());
                }

                routingContext.response().end(jsonResult.encode());
            }
        });
    }
}
