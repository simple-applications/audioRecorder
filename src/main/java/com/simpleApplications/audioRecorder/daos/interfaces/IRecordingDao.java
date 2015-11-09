package com.simpleApplications.audioRecorder.daos.interfaces;

import com.simpleApplications.audioRecorder.daos.mappers.RecordingMapper;
import com.simpleApplications.audioRecorder.model.Recording;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
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
    List<Recording> getByProjectId(@Bind("projectId") int projectId);

    @SqlUpdate("INSERT INTO recordings(createdAt, fileName, length, size, converted, projectId) VALUES (:createdAt, :fileName, :length, :size, :converted, :projectId)")
    int create(@BindBean Recording recording);

    @SqlUpdate("UPDATE recordings SET createdAt = :createdAt, fileName = :fileName, length = :length, size = :size, converted = :converted, projectId = :projectId WHERE id = :id")
    void update(@BindBean Recording recording);

    @SqlUpdate("DELETE FROM recordings WHERE id = :recordingId")
    void delete(@BindBean Recording recording);
}
