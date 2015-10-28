package com.simpleApplications.audioRecorder.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nico Moehring
 */
public class Recording {

    public int id;

    @NotEmpty
    public long createdAt;

    @NotEmpty
    @Size(max = 24)
    public String fileName;

    @NotNull
    public boolean converted;

    @Min(1)
    public int size;

    @Min(1)
    public int length;

    @NotNull
    public int projectId;
}
