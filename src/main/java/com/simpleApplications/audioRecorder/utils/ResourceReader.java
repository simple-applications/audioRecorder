package com.simpleApplications.audioRecorder.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * @author Nico Moehring
 */
public interface ResourceReader {
    default InputStream getResourceAsStream(final String resourcePath) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        return classLoader.getResourceAsStream(resourcePath);
    }

    default String getFileContentAsString(final String resourcePath) throws IOException {
        return IOUtils.toString(this.getResourceAsStream(resourcePath));
    }

    default byte[] getFileContentAsByteArray(final String resourcePath) throws IOException {
        return IOUtils.toByteArray(this.getResourceAsStream(resourcePath));
    }
}
