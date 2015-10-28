package com.simpleApplications.audioRecorder.daos.interfaces;

import com.simpleApplications.audioRecorder.daos.mappers.RecordingProjectMapper;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 * @author Nico Moehring
 */
public interface IRecordingProjectDao {
    @SqlQuery("SELECT * FROM recordingProjects ORDER BY id DESC")
    @Mapper(RecordingProjectMapper.class)
    public List<RecordingProject> getAll();

    @SqlUpdate("INSERT INTO recordingProjects(name, referenceAudioFileName) VALUES (:name, :referenceAudioFileName)")
    public int create(@BindBean RecordingProject project);

    @SqlUpdate("UPDATE recordingProjects SET name = :name, referenceAudioFileName = :referenceAudioFileName WHERE id = :id")
    public void update(@BindBean RecordingProject project);

    @SqlUpdate("DELETE FROM recordingProjects WHERE id = :id")
    public void delete(@BindBean RecordingProject project);
}
