package com.simpleApplications.audioRecorder.daos;

import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingDao;
import com.simpleApplications.audioRecorder.model.Recording;
import io.vertx.ext.sql.SQLConnection;

/**
 * @author Nico Moehring
 */
public class RecordingDao extends AbstractDao<Recording> implements IRecordingDao {
    public RecordingDao(SQLConnection connection) {
        super(connection);
    }

    @Override
    public void save(Recording entity) {

    }

    @Override
    public void delete(Recording entity) {

    }
}
