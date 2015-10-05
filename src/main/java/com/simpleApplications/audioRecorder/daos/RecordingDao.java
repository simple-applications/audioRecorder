package com.simpleApplications.audioRecorder.daos;

import com.simpleApplications.audioRecorder.messages.DatabaseRequest;
import io.vertx.ext.sql.SQLConnection;

/**
 * @author Nico Moehring
 */
public class RecordingDao extends AbstractDao {
    public RecordingDao(SQLConnection connection) {
        super(connection);

        this.functionMap.put(DatabaseInteractions.FIND_BY_PROJECT_ID, this::findByProjectId);
    }

    @Override
    protected void getAll(DatabaseRequest request) {

    }

    @Override
    protected void save(DatabaseRequest request) {

    }

    @Override
    protected void delete(DatabaseRequest request) {

    }

    /**
     *
     * @param request
     */
    protected void findByProjectId(DatabaseRequest request) {

    }
}
