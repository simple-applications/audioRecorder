package com.simpleApplications.audioRecorder.daos.mappers;

import com.simpleApplications.audioRecorder.model.RecordingProject;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Nico Moehring
 */
public class RecordingProjectMapper implements ResultSetMapper<RecordingProject> {
    @Override
    public RecordingProject map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        RecordingProject recordingProject = new RecordingProject();
        recordingProject.id = resultSet.getInt("id");
        recordingProject.name = resultSet.getString("name");
        recordingProject.referenceAudioFileName = resultSet.getString("referenceAudioFileName");

        return recordingProject;
    }
}
