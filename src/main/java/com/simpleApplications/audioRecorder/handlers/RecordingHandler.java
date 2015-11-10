package com.simpleApplications.audioRecorder.handlers;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingDao;
import com.simpleApplications.audioRecorder.exceptions.EntityNotFoundException;
import com.simpleApplications.audioRecorder.exceptions.HttpException;
import com.simpleApplications.audioRecorder.helper.interfaces.IFileHelper;
import com.simpleApplications.audioRecorder.model.Recording;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;

import javax.validation.Validator;
import java.util.List;

/**
 * @author Nico Moehring
 */
public class RecordingHandler extends AbstractRequestHandler<Recording> {

    protected IRecordingDao recordingDao;

    protected IFileHelper fileHelper;

    @Inject
    public RecordingHandler(Validator validator, IRecordingDao recordingDao, IFileHelper fileHelper) {
        super(validator);

        this.recordingDao = recordingDao;
        this.fileHelper = fileHelper;
        this.handledMethods.put(HttpMethod.GET, this::getRecordings);
        this.handledMethods.put(HttpMethod.DELETE, this::deleteRecording);

        this.routes.add("/recordings");
        this.routes.add("/recordings/:recordingId");
    }

    protected void deleteRecording(RoutingContext routingContext) throws HttpException{
        try {
            final int recordingId = Integer.valueOf(routingContext.request().getParam("recordingId"));

            Vertx.currentContext().executeBlocking((Future<Void> future) -> {
                final Recording recording = this.recordingDao.getById(recordingId);

                if (recording == null) {
                    future.fail(new EntityNotFoundException());
                }

                this.recordingDao.delete(recording);
                this.fileHelper.deleteRecording(recording);
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

    protected void getRecordings(RoutingContext routingContext) {
        try {
            final int projectId = Integer.valueOf(routingContext.request().getFormAttribute("projectId"));

            Vertx.currentContext().executeBlocking((Future<List<Recording>> objectFuture) -> {
                objectFuture.complete(this.recordingDao.getByProjectId(projectId));
            }, result -> {
                if (result.succeeded()) {
                    routingContext.response().end(
                            this.generateJsonArray(result.result()).toString()
                    );
                }
            });
        } catch (NumberFormatException e) {
            routingContext.response().end(new JsonArray().toString());
        }
    }
}
