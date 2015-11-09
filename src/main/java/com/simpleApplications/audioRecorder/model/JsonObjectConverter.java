package com.simpleApplications.audioRecorder.model;

import io.vertx.core.json.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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

    default void bindJson(JsonObject jsonData) {
        Arrays.stream(this.getClass().getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .forEach(method -> {
                    String attribute = method.getName().substring(3).toLowerCase();

                    try {
                        Object attributeValue = jsonData.getValue(attribute);

                        if (attributeValue != null) {
                            method.invoke(this, attributeValue);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }
}
