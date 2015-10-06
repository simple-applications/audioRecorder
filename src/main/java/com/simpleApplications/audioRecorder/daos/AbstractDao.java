package com.simpleApplications.audioRecorder.daos;

import com.simpleApplications.audioRecorder.daos.interfaces.IDao;
import io.vertx.ext.sql.SQLConnection;

/**
 * @author Nico Moehring
 */
public abstract class AbstractDao<T> implements IDao<T> {
    protected final SQLConnection connection;

    public AbstractDao(SQLConnection connection) {
        this.connection = connection;
    }
}
