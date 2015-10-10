package com.simpleApplications.audioRecorder.daos;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.daos.interfaces.IDatabaseUpdater;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

/**
 * @author Nico Moehring
 */
public class DatabaseUpdater implements IDatabaseUpdater {

    /**
     *
     */
    protected DBI database;

    /**
     *
     * @param database
     */
    @Inject
    public DatabaseUpdater(DBI database) {
        this.database = database;
    }

    @Override
    public void updateDatabaseStructure() {
        final Handle databaseHandle = this.database.open();

        databaseHandle.createStatement("CREATE TABLE IF NOT EXISTS recordingProjects(id INTEGER IDENTITY, name VARCHAR(255))").execute();
        databaseHandle.createStatement("CREATE TABLE IF NOT EXISTS recordings(id INTEGER IDENTITY, createdAt BIGINT NOT NULL, fileName VARCHAR(20) NOT NULL, length INTEGER NOT NULL, size INTEGER NOT NULL, projectId INTEGER NOT NULL)").execute();

        try {
            databaseHandle.createStatement("ALTER TABLE recordings ADD FOREIGN KEY (projectId) REFERENCES recordingProjects (id)").execute();
        } catch (UnableToExecuteStatementException e) { }

        databaseHandle.close();
    }
}
