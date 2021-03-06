package com.simpleApplications.audioRecorder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simpleApplications.audioRecorder.daos.interfaces.IDatabaseUpdater;
import com.simpleApplications.audioRecorder.exceptions.InitializeException;
import com.simpleApplications.audioRecorder.guice.GuiceModule;
import com.simpleApplications.audioRecorder.utils.ResourceReader;
import com.simpleApplications.audioRecorder.verticles.HttpVertical;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Nico Moehring
 */
public class Launcher implements ResourceReader {

    private static final String CONFIG_VERTICALS = "verticals";

    private static final String CONFIG_WORKER_VERTICALS = "workerVerticals";

    private static final String CONFIG_PARAMETERS = "parameters";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Injector injector;

    private Vertx vertx;

    public static void main(String args[]) {
        new Launcher().start();
    }

    /**
     *
     */
    public void start() {
        try {
            this.vertx = Vertx.vertx();

            final JsonObject config = this.getConfig();

            this.injector = Guice.createInjector(new GuiceModule(
                    this.getConfigParameters(config)
            ));

            this.injector.getInstance(IDatabaseUpdater.class).updateDatabaseStructure();

            this.startVerticals(config, Launcher.CONFIG_VERTICALS, false);
            this.startVerticals(config, Launcher.CONFIG_WORKER_VERTICALS, true);

        } catch (InitializeException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param config
     * @param configKey
     * @param worker
     */
    private void startVerticals(final JsonObject config, final String configKey, final boolean worker) throws InitializeException {
        if (config.containsKey(configKey)) {
            final JsonArray verticals = config.getJsonArray(configKey);

            for (int i = 0; i < verticals.size(); i++) {
                final DeploymentOptions options = new DeploymentOptions().setWorker(worker);
                options.setConfig(config);

                try {
                    this.logger.info("Starting vertical: " + verticals.getString(i));
                    Class<AbstractVerticle> verticleClass = (Class<AbstractVerticle>) Class.forName(verticals.getString(i));

                    this.vertx.deployVerticle(this.injector.getInstance(verticleClass), options);
                } catch (ClassNotFoundException e) {
                    throw new InitializeException("The vertical " + verticals.getString(i) + " could not be found: " + e.getMessage());
                }
            }
        } else {
            this.logger.warn("Could not find vertical list with the name: " + configKey);
        }
    }

    /**
     *
     * @return
     * @throws InitializeException
     */
    private JsonObject getConfig() throws InitializeException {
        try {
            return new JsonObject(
                    new Scanner(
                            this.getResourceAsStream("config/config.json")
                    ).useDelimiter("\\A").next()
            );
        } catch (NullPointerException e) {
            throw new InitializeException("Config file could not be found!");
        }
    }

    private Map<String, String> getConfigParameters(JsonObject config) {
        final Map<String, String> result = new HashMap<>();

        JsonObject parameters = config.getJsonObject(Launcher.CONFIG_PARAMETERS);

        if (parameters != null) {
            parameters.fieldNames().forEach(fieldName -> {
                result.put(fieldName, parameters.getString(fieldName));
            });
        }

        return result;
    }
}
