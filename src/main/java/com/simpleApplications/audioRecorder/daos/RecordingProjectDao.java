package com.simpleApplications.audioRecorder.daos;

import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import io.vertx.ext.sql.SQLConnection;

/**
 * @author Nico Moehring
 */
public class RecordingProjectDao extends AbstractDao<RecordingProject> implements IRecordingProjectDao {

    public RecordingProjectDao(SQLConnection connection) {
        super(connection);
    }

    @Override
    public void save(RecordingProject entity) {

    }

    @Override
    public void delete(RecordingProject entity) {

    }
}
