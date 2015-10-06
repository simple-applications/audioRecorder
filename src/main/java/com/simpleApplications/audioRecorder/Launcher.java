package com.simpleApplications.audioRecorder;

import com.simpleApplications.audioRecorder.exceptions.InitializeException;
import com.simpleApplications.audioRecorder.verticles.HttpVertical;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
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
public class Launcher {

    private static final String CONFIG_VERTICALS = "verticals";

    private static final String CONFIG_WORKER_VERTICALS = "workerVerticals";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String args[]) {
        new Launcher().start();
    }

    /**
     *
     */
    public void start() {
        try {
            final JsonObject config = this.getConfig();

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
    private void startVerticals(final JsonObject config, final String configKey, final boolean worker) {
        if (config.containsKey(configKey)) {
            final JsonArray workerVerticals = config.getJsonArray(configKey);

            for (int i = 0; i < workerVerticals.size(); i++) {
                final DeploymentOptions options = new DeploymentOptions().setWorker(worker);
                options.setConfig(config);

                this.logger.info("Starting vertical: " + workerVerticals.getString(i));
                Vertx.vertx().deployVerticle(workerVerticals.getString(i), options);
            }
        }
    }

    /**
     *
     * @return
     * @throws InitializeException
     */
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
