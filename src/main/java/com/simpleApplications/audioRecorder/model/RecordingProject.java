package com.simpleApplications.audioRecorder.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nico Moehring
 */
public class RecordingProject implements JsonObjectConverter {
    public int id;

    @NotEmpty
    @Size(max = 255)
    public String name;

    @NotEmpty
    @Size(max = 255)
    public String referenceAudioFileName;
}
