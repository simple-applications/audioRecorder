package com.simpleApplications.audioRecorder.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nico Moehring
 */
public class RecordingProject implements JsonObjectConverter {
    public int id;

    @NotNull
    @Size(max = 255)
    public String name;

    @Size(max = 255)
    public String referenceAudioFileName;
}
