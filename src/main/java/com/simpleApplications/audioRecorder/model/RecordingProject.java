package com.simpleApplications.audioRecorder.model;

import com.simpleApplications.audioRecorder.model.constraints.UniqueProjectNameConstraint;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nico Moehring
 */
public class RecordingProject implements JsonObjectConverter {
    protected int id;

    @NotEmpty
    @Size(max = 255)
    @UniqueProjectNameConstraint
    protected String name;

    protected int referenceFileId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReferenceFileId() {
        return referenceFileId;
    }

    public void setReferenceFileId(int referenceFileId) {
        this.referenceFileId = referenceFileId;
    }
}
