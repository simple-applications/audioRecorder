package com.simpleApplications.audioRecorder.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nico Moehring
 */
public class Recording {

    public int id;

    @NotNull
    public long createdAt;

    @NotNull
    @Size(max = 20)
    public String fileName;

    @NotNull
    public int projectId;
}
