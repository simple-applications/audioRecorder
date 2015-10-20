package com.simpleApplications.audioRecorder.model;

import io.vertx.core.json.JsonObject;

import java.lang.reflect.Field;

/**
 * @author Nico Moehring
 */
public interface JsonObjectConverter {
    default JsonObject toJson() {
        final JsonObject result = new JsonObject();

        for (Field tmpField: this.getClass().getDeclaredFields()) {
            try {
                result.put(tmpField.getName(), tmpField.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
