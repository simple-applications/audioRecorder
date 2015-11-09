package com.simpleApplications.audioRecorder.daos.interfaces;

import com.simpleApplications.audioRecorder.daos.mappers.RecordingProjectMapper;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 * @author Nico Moehring
 */
public interface IRecordingProjectDao {
    @SqlQuery("SELECT * FROM recordingProjects ORDER BY id DESC")
    @Mapper(RecordingProjectMapper.class)
    List<RecordingProject> getAll();

    @SqlQuery("SELECT * FROM recordingProjects WHERE name = :name LIMIT 1")
    @Mapper(RecordingProjectMapper.class)
    RecordingProject getByName(@Bind("name") String name);

    @SqlQuery("SELECT * FROM recordingProjects WHERE id = :id LIMIT 1")
    @Mapper(RecordingProjectMapper.class)
    RecordingProject getById(@Bind("id") int id);

    @SqlUpdate("INSERT INTO recordingProjects(name, referenceFileId) VALUES (:name, :referenceFileId)")
    @GetGeneratedKeys
    int create(@BindBean RecordingProject project);

    @SqlUpdate("UPDATE recordingProjects SET name = :name, referenceFileId = :referenceFileId WHERE id = :id")
    void update(@BindBean RecordingProject project);

    @SqlUpdate("DELETE FROM recordingProjects WHERE id = :id")
    void delete(@BindBean RecordingProject project);

    @SqlUpdate("DELETE FROM recordingProjects")
    void deleteAll();
}
