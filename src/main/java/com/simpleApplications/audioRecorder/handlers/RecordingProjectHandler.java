package com.simpleApplications.audioRecorder.handlers;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.exceptions.ValidationException;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * @author Nico Moehring
 */
public class RecordingProjectHandler extends AbstractRequestHandler<RecordingProject> {

    protected IRecordingProjectDao recordingProjectDao;

    @Inject
    public RecordingProjectHandler(IRecordingProjectDao recordingProjectDao, Validator validator) {
        super(validator);

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

    protected void createRecordingProject(RoutingContext routingContext) throws ValidationException {
        final RecordingProject recordingProject = new RecordingProject();
        recordingProject.bindJson(this.getJsonData(routingContext.request()));

        final Set<ConstraintViolation<RecordingProject>> constraintViolations = this.validator.validate(recordingProject);

        if (constraintViolations.isEmpty()) {

        } else {
            this.generateValidationException(constraintViolations);
        }
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

    protected JsonObject getJsonData(HttpServerRequest request) {
        final String jsonData = request.getParam("data");

        if (null != jsonData && !jsonData.isEmpty()) {
            return new JsonObject(jsonData);
        } else {
            return null;
        }
    }
}
