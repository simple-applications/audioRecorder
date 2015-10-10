package com.simpleApplications.audioRecorder.actions.interfaces;

import com.simpleApplications.audioRecorder.model.Recording;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author Nico Moehring
 */
public interface IActionFactory {
    /**
     *
     * @param data
     * @return
     */
    public Handler<Future<Recording>> getCreateRecordingAction(JsonObject data);

    /**
     *
     * @param recordingId
     * @return
     */
    public Handler<Future<Void>> getDeleteRecordingAction(int recordingId);

    /**
     *
     * @param data
     * @return
     */
    public Handler<Future<RecordingProject>> getCreateRecordingProjectAction(JsonObject data);

    /**
     *
     * @param projectId
     * @return
     */
    public Handler<Future<Void>> getDeleteRecordingProjectAction(int projectId);

    /**
     *
     * @param projectId
     * @return
     */
    public Handler<Future<List<Recording>>> getRecordingsForProjectAction(int projectId);

    /**
     *
     * @return
     */
    public Handler<Future<List<RecordingProject>>> getAllRecordingProjectsAction();
}
