package com.simpleApplications.audioRecorder.daos;

import com.simpleApplications.audioRecorder.exceptions.DatabaseException;
import com.simpleApplications.audioRecorder.messages.DatabaseRequest;
import io.vertx.ext.sql.SQLConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Nico Moehring
 */
public abstract class AbstractDao implements IDao {
    protected final SQLConnection connection;

    /**
     *
     */
    protected Map<DatabaseInteractions, Consumer<DatabaseRequest>> functionMap = new HashMap<>();

    public AbstractDao(SQLConnection connection) {
        this.connection = connection;

        this.functionMap.put(DatabaseInteractions.GET_ALL, this::getAll);
        this.functionMap.put(DatabaseInteractions.SAVE, this::save);
        this.functionMap.put(DatabaseInteractions.DELETE, this::delete);
    }

    @Override
    public void handleDatabaseRequest(DatabaseRequest request) throws DatabaseException {
        if (this.functionMap.containsKey(request.type)) {
            this.functionMap.get(request.type).accept(request);
        } else {
            throw new DatabaseException("No handler found for class " + request.objectClass.getCanonicalName() + " and operation " + request.type.toString());
        }
    }

    /**
     *
     * @param request
     */
    protected abstract void getAll(DatabaseRequest request);

    /**
     *
     * @param request
     */
    protected abstract void save(DatabaseRequest request);

    /**
     *
     * @param request
     */
    protected abstract void delete(DatabaseRequest request);
}
