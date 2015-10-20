package com.simpleApplications.audioRecorder.daos.interfaces;

import com.simpleApplications.audioRecorder.daos.mappers.RecordingProjectMapper;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 * @author Nico Moehring
 */
public interface IRecordingProjectDao {
    @SqlQuery("SELECT * FROM recordingProjects ORDER BY id DESC")
    @Mapper(RecordingProjectMapper.class)
    public List<RecordingProject> getAll();
}
