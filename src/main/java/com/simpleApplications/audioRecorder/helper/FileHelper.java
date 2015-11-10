package com.simpleApplications.audioRecorder.helper;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.simpleApplications.audioRecorder.helper.interfaces.IFileHelper;
import com.simpleApplications.audioRecorder.model.Recording;

import java.io.File;

/**
 * @author Nico Moehring
 */
public class FileHelper implements IFileHelper {

    protected String uncompressedRecordingsPath;

    protected String compressedRecordingsPath;

    protected String referenceFilesPath;

    @Inject
    public FileHelper(@Named("uncompressedRecordingsPath") String uncompressedRecordingsPath,
                      @Named("compressedRecordingsPath") String compressedRecordingsPath,
                      @Named("referenceFilesPath") String referenceFilesPath) {
        this.uncompressedRecordingsPath = this.appendPathSeparatorIfNecessary(uncompressedRecordingsPath);
        this.compressedRecordingsPath = this.appendPathSeparatorIfNecessary(compressedRecordingsPath);
        this.referenceFilesPath = this.appendPathSeparatorIfNecessary(referenceFilesPath);
    }

    @Override
    public boolean deleteRecording(Recording recording) {
        return this.deleteFile(this.uncompressedRecordingsPath + recording.fileName) && this.deleteFile(this.compressedRecordingsPath + recording.fileName);
    }

    @Override
    public boolean deleteReferenceFile(String fileName) {
        return this.deleteFile(this.referenceFilesPath + fileName);
    }

    protected String appendPathSeparatorIfNecessary(String path) {
        if (path.endsWith(File.separator)) {
            return path;
        } else {
            return path + File.separator;
        }
    }

    protected boolean deleteFile(String filePath) {
        final File targetFile = new File(filePath);

        if (targetFile.exists()) {
            return targetFile.delete();
        }

        return true;
    }
}
