package com.simpleApplications.audioRecorder.daos;

import com.simpleApplications.audioRecorder.exceptions.DatabaseException;
import com.simpleApplications.audioRecorder.messages.DatabaseRequest;

/**
 * @author Nico Moehring
 */
public interface IDao {
    public void handleDatabaseRequest(DatabaseRequest request) throws DatabaseException;
}
