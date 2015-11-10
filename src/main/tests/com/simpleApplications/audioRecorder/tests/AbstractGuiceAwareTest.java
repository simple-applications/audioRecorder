package com.simpleApplications.audioRecorder.tests;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simpleApplications.audioRecorder.guice.GuiceModule;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nico Moehring
 */
public abstract class AbstractGuiceAwareTest {

    protected Injector injector;

    @Before
    public void setUp() throws Exception {
        final Map<String, String> testParameters = new HashMap<>();
        testParameters.put("uncompressedRecordingsPath", "test/uncompressedRecordings");
        testParameters.put("compressedRecordingsPath", "test/compressedRecordingsPath");
        testParameters.put("referenceFilesPath", "test/referenceFilesPath");

        this.injector = Guice.createInjector(new GuiceModule(testParameters));
    }
}
