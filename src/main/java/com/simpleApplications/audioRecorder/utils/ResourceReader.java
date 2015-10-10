package com.simpleApplications.audioRecorder.utils;

import java.io.InputStream;

/**
 * @author Nico Moehring
 */
public interface ResourceReader {
    default InputStream getResourceAsStream(final String resourcePath) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        return classLoader.getResourceAsStream(resourcePath);
    }
}
