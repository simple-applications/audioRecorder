package com.simpleApplications.audioRecorder.daos.interfaces;

import com.simpleApplications.audioRecorder.daos.mappers.RecordingMapper;
import com.simpleApplications.audioRecorder.model.Recording;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 * @author Nico Moehring
 */
public interface IRecordingDao {
    @SqlQuery("SELECT * FROM recordings WHERE projectId = :projectId")
    @Mapper(RecordingMapper.class)
    public List<Recording> getByProjectId(@Bind("projectId") int projectId);

    @SqlUpdate("INSERT INTO recordings(createdAt, fileName, length, size, projectId) ")
    public int insert(int createdAt, String fileName, int length, int size, int projectId);
}
