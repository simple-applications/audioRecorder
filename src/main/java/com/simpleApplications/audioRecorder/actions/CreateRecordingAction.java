package com.simpleApplications.audioRecorder.actions;

import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingDao;
import com.simpleApplications.audioRecorder.model.Recording;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * Created by nico on 06.10.2015.
 */
public class CreateRecordingAction implements Handler<Future<Recording>> {

    private JsonObject data;

    private IRecordingDao recordingDao;

    public CreateRecordingAction(IRecordingDao recordingDao, JsonObject data) {
        this.recordingDao = recordingDao;
        this.data = data;
    }

    @Override
    public void handle(Future<Recording> recordingFuture) {

    }
}
