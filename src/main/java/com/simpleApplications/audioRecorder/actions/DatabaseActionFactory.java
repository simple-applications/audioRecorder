package com.simpleApplications.audioRecorder.actions;

import com.simpleApplications.audioRecorder.actions.interfaces.IActionFactory;
import com.simpleApplications.audioRecorder.model.Recording;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author Nico Moehring
 */
public class DatabaseActionFactory implements IActionFactory {
    @Override
    public Handler<Future<Recording>> getCreateRecordingAction(JsonObject data) {
        return null;
    }

    @Override
    public Handler<Future<Void>> getDeleteRecordingAction(int recordingId) {
        return null;
    }

    @Override
    public Handler<Future<RecordingProject>> getCreateRecordingProjectAction(JsonObject data) {
        return null;
    }

    @Override
    public Handler<Future<Void>> getDeleteRecordingProjectAction(int projectId) {
        return null;
    }

    @Override
    public Handler<Future<List<Recording>>> getRecordingsForProjectAction(int projectId) {
        return null;
    }

    @Override
    public Handler<Future<List<RecordingProject>>> getAllRecordingProjectsAction() {
        return null;
    }
}
