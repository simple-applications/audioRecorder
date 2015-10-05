package com.simpleApplications.audioRecorder.daos;

import com.simpleApplications.audioRecorder.exceptions.DatabaseException;
import com.simpleApplications.audioRecorder.messages.DatabaseRequest;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import io.vertx.ext.sql.SQLConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Nico Moehring
 */
public class RecordingProjectDao extends AbstractDao {

    public RecordingProjectDao(SQLConnection connection) {
        super(connection);

        this.functionMap.put(DatabaseInteractions.FIND_BY_PROJECT_ID, this::findById);
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
    protected void findById(DatabaseRequest request) {

    }
}
