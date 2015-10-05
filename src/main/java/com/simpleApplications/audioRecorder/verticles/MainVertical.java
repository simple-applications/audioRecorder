package com.simpleApplications.audioRecorder.verticles;

import com.simpleApplications.audioRecorder.exceptions.InitializeException;
import com.simpleApplications.audioRecorder.messages.MessageChannels;
import com.simpleApplications.audioRecorder.messages.SystemInformation;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Nico Moehring
 */
public class MainVertical extends AbstractVerticle {

    private static final String CONFIG_WORKER_VERTICALS = "workerVerticals";

    private JsonObject config;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void start() throws Exception {
        this.config = this.getConfig();

        this.startHttpServer();
        this.startWorkerVerticals();
    }

    private void startHttpServer() {

    }

    private void startWorkerVerticals() {
        if (this.config.containsKey(MainVertical.CONFIG_WORKER_VERTICALS)) {
            final JsonArray workerVerticals = this.config.getJsonArray(MainVertical.CONFIG_WORKER_VERTICALS);

            for (int i = 0; i < workerVerticals.size(); i++) {
                final DeploymentOptions options = new DeploymentOptions().setWorker(true);

                this.logger.info("Starting vertical: " + workerVerticals.getString(i));
                vertx.deployVerticle(workerVerticals.getString(i), options);
            }
        } else {
            // TODO Add Warning
        }
    }

    private JsonObject getConfig() throws InitializeException {
        try {
            final ClassLoader classLoader = this.getClass().getClassLoader();
            final File configFile = new File(classLoader.getResource("config/config.json").getFile());

            return new JsonObject(new Scanner(configFile).useDelimiter("\\A").next());
        } catch (NullPointerException e) {
            throw new InitializeException("Config file could not be found!");
        } catch (FileNotFoundException e) {
            throw new InitializeException("Could not read the config file due to: " + e.getMessage());
        }
    }
}
