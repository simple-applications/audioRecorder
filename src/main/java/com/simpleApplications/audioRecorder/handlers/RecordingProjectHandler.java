package com.simpleApplications.audioRecorder.handlers;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.exceptions.EntityNotFoundException;
import com.simpleApplications.audioRecorder.exceptions.NoDataGivenException;
import com.simpleApplications.audioRecorder.exceptions.ValidationException;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.validation.Validator;
import java.util.List;

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
        this.handledMethods.put(HttpMethod.PUT, this::saveRecordingProject);
        this.handledMethods.put(HttpMethod.POST, this::createRecordingProject);
        this.handledMethods.put(HttpMethod.DELETE, this::deleteRecordingProject);

        this.routes.add("/recordingProjects");
        this.routes.add("/recordingProjects/:projectId");
    }

    protected void deleteRecordingProject(RoutingContext routingContext) {

    }

    protected void saveRecordingProject(RoutingContext routingContext) throws NoDataGivenException, EntityNotFoundException {
        try {
            int projectId = Integer.valueOf(routingContext.request().getParam("projectId"));
            RecordingProject requestData = this.getEntityFromRequest(routingContext);

            this.handleObjectRequest(routingContext, (Future<RecordingProject> objectFuture) -> {
                try {
                    RecordingProject recordingProject = this.recordingProjectDao.getById(projectId);

                    if (recordingProject == null) {
                        throw new EntityNotFoundException("No recording project found with ID " + projectId);
                    }

                    recordingProject.bindJson(requestData.toJson());
                    this.validateEntity(recordingProject);

                    this.recordingProjectDao.update(recordingProject);
                    objectFuture.complete(recordingProject);
                } catch (ValidationException | EntityNotFoundException e) {
                    objectFuture.fail(e);
                }
            });
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("No recording project found with ID " + routingContext.request().getParam("projectId"));
        }
    }

    protected void createRecordingProject(RoutingContext routingContext) throws NoDataGivenException {
        final RecordingProject recordingProject = this.getEntityFromRequest(routingContext);

        this.handleObjectRequest(routingContext, (Future<RecordingProject> objectFuture) -> {
            try {
                this.validateEntity(recordingProject);

                recordingProject.setId(this.recordingProjectDao.create(recordingProject));
                objectFuture.complete(recordingProject);
            } catch (ValidationException e) {
                objectFuture.fail(e);
            }
        });
    }

    protected void handleObjectRequest(RoutingContext routingContext, Handler<Future<RecordingProject>> handler) throws NoDataGivenException {
        Vertx.currentContext().executeBlocking(handler, result -> {
            if (result.succeeded()) {
                this.sendEntityResponse(routingContext, result.result());
            } else if (result.failed()) {
                Throwable error = result.cause();

                if (error instanceof ValidationException) {
                    this.handleValidationErrors(routingContext, (ValidationException) result.cause());
                } else if (error instanceof EntityNotFoundException) {
                    this.handleEntityNotFoundException(routingContext);
                }
            }
        });
    }

    protected void getRecordingProjects(RoutingContext routingContext) {
        Vertx.currentContext().executeBlocking((Future<List<RecordingProject>> objectFuture) -> {
            objectFuture.complete(this.recordingProjectDao.getAll());
        }, result -> {
            if (result.succeeded()) {
                routingContext.response().end(
                        this.generateJsonArray(result.result()).toString()
                );
            }
        });
    }
}
