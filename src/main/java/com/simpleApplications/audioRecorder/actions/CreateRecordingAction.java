package com.simpleApplications.audioRecorder.actions;

import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingDao;
import com.simpleApplications.audioRecorder.model.Recording;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * @author Nico Moehring
 */
public class CreateRecordingAction implements Handler<Future<Recording>> {

    private JsonObject data;

    private IRecordingDao recordingDao;

    public CreateRecordingAction(IRecordingDao recordingDao, JsonObject data) {
        this.recordingDao = recordingDao;
    }

    @Override
    public void handle(Future<Recording> recordingFuture) {

    }
}
