package com.simpleApplications.audioRecorder.tests;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simpleApplications.audioRecorder.guice.GuiceModule;
import org.junit.Before;

/**
 * @author Nico Moehring
 */
public abstract class AbstractGuiceAwareTest {

    protected Injector injector;

    @Before
    public void setUp() throws Exception {
        this.injector = Guice.createInjector(new GuiceModule());
    }
}
