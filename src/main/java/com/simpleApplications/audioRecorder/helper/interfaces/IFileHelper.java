package com.simpleApplications.audioRecorder.helper.interfaces;

import com.simpleApplications.audioRecorder.model.Recording;

/**
 * @author Nico Moehring
 */
public interface IFileHelper {
    boolean deleteRecording(Recording recording);

    boolean deleteReferenceFile(String fileName);
}
