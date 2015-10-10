package com.simpleApplications.audioRecorder.daos.mappers;

import com.simpleApplications.audioRecorder.model.Recording;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by nico on 07.10.2015.
 */
public class RecordingMapper implements ResultSetMapper<Recording> {
    @Override
    public Recording map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return null;
    }
}
