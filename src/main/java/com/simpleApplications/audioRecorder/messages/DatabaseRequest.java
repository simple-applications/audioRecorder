package com.simpleApplications.audioRecorder.messages;

import com.simpleApplications.audioRecorder.daos.DatabaseInteractions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author Nico Moehring
 */
public class DatabaseRequest {
    public Class objectClass;

    public DatabaseInteractions type;

    public Message<DatabaseRequest> originalRequest;

    public JsonObject data;
}
