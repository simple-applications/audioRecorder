package com.simpleApplications.audioRecorder.handlers;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.exceptions.EntityNotFoundException;
import com.simpleApplications.audioRecorder.exceptions.HttpException;
import com.simpleApplications.audioRecorder.exceptions.NoDataGivenException;
import com.simpleApplications.audioRecorder.exceptions.ValidationException;
import com.simpleApplications.audioRecorder.model.Recording;
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

    protected void deleteRecordingProject(RoutingContext routingContext) throws HttpException {
        try {
            int projectId = Integer.valueOf(routingContext.request().getParam("projectId"));

            Vertx.currentContext().executeBlocking((Future<Void> future) -> {
                RecordingProject recordingProject = this.recordingProjectDao.getById(projectId);

                if (recordingProject != null) {
                    this.recordingProjectDao.delete(recordingProject);
                    future.complete();
                } else {
                    future.fail(new EntityNotFoundException());
                }
            }, result -> {
                if (result.succeeded()) {
                    routingContext.response().end();
                } else {
                    this.handleHttpException(routingContext, (HttpException) result.cause());
                }
            });
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException();
        }
    }

    protected void saveRecordingProject(RoutingContext routingContext) throws HttpException {
        try {
            RecordingProject requestData = this.getEntityFromRequest(routingContext);
            int projectId = Integer.valueOf(routingContext.request().getParam("projectId"));

            this.handleObjectRequest(routingContext, (Future<RecordingProject> objectFuture) -> {
                try {
                    RecordingProject recordingProject = this.recordingProjectDao.getById(projectId);

                    if (recordingProject == null) {
                        throw new EntityNotFoundException();
                    }

                    recordingProject.setName(requestData.getName());
                    recordingProject.setReferenceFileId(requestData.getReferenceFileId());
                    this.validateEntity(recordingProject);

                    this.recordingProjectDao.update(recordingProject);
                    objectFuture.complete(recordingProject);
                } catch (ValidationException | EntityNotFoundException e) {
                    objectFuture.fail(e);
                }
            });
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException();
        }
    }

    protected void createRecordingProject(RoutingContext routingContext) throws HttpException {
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

    protected void handleObjectRequest(RoutingContext routingContext, Handler<Future<RecordingProject>> handler) {
        Vertx.currentContext().executeBlocking(handler, result -> {
            if (result.succeeded()) {
                this.sendEntityResponse(routingContext, result.result());
            } else if (result.failed()) {
                this.handleHttpException(routingContext, (HttpException) result.cause());
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
