package com.simpleApplications.audioRecorder.daos.interfaces;

import com.simpleApplications.audioRecorder.model.RecordingProject;

import java.util.List;

/**
 * @author Nico Moehring
 */
public interface IRecordingProjectDao {
    public List<RecordingProject> getAll();
}
