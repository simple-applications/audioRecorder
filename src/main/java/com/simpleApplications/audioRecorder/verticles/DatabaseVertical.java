package com.simpleApplications.audioRecorder.verticles;

import com.simpleApplications.audioRecorder.daos.IDao;
import com.simpleApplications.audioRecorder.daos.RecordingDao;
import com.simpleApplications.audioRecorder.daos.RecordingProjectDao;
import com.simpleApplications.audioRecorder.exceptions.DatabaseException;
import com.simpleApplications.audioRecorder.messages.DatabaseRequest;
import com.simpleApplications.audioRecorder.messages.MessageChannels;
import com.simpleApplications.audioRecorder.model.Recording;
import com.simpleApplications.audioRecorder.model.RecordingProject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nico Moehring
 */
public class DatabaseVertical extends AbstractVerticle {

    /**
     *
     */
    private final static String DATABASE_NAME = "db/testdb";

    /**
     *
     */
    private JDBCClient client;

    /**
     *
     */
    private SQLConnection connection;

    /**
     *
     */
    private Map<Class, IDao> databaseHandlers = new HashMap<>();

    @Override
    public void start() throws Exception {
        this.client = JDBCClient.createShared(this.vertx, this.getDatabaseConfig());

        this.client.getConnection(result -> {
            if (result.succeeded()) {
                this.connection = result.result();

                this.databaseHandlers.put(Recording.class, new RecordingDao(this.connection));
                this.databaseHandlers.put(RecordingProject.class, new RecordingProjectDao(this.connection));

                this.initDatabase();
                this.vertx.eventBus().consumer(MessageChannels.DATABASE_INTERACTIONS.toString(), this::handleDatabaseRequest);
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        this.client.close();
    }

    /**
     *
     * @return
     */
    private JsonObject getDatabaseConfig() {
        return new JsonObject()
                .put("url", "jdbc:hsqldb:file:" + DATABASE_NAME)
                .put("driver_class", "org.hsqldb.jdbcDriver")
                .put("use", "SA")
                .put("password", "");
    }

    /**
     *
     */
    private void initDatabase() {
        connection.execute("CREATE TABLE IF NOT EXISTS xyx (a int, b varchar(10))", result -> {

        });
    }

    /**
     *
     * @param message
     */
    private void handleDatabaseRequest(Message<DatabaseRequest> message) {
        final DatabaseRequest request = message.body();

        try {
            if (this.databaseHandlers.containsKey(request.objectClass)) {
                this.databaseHandlers.get(request.objectClass).handleDatabaseRequest(request);
            } else {
                throw new DatabaseException("No database handler found for class " + request.getClass().getCanonicalName());
            }
        } catch (DatabaseException e) {
            // TODO Send error.
        }
    }
}
