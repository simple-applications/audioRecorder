package com.simpleApplications.audioRecorder.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.simpleApplications.audioRecorder.actions.DatabaseActionFactory;
import com.simpleApplications.audioRecorder.actions.interfaces.IActionFactory;
import com.simpleApplications.audioRecorder.daos.DatabaseUpdater;
import com.simpleApplications.audioRecorder.daos.interfaces.IDatabaseUpdater;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingDao;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.handlers.IndexHandler;
import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import org.skife.jdbi.v2.DBI;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

/**
 * Created by nico on 07.10.2015.
 */
public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            final DatabaseActionFactory actionFactory = new DatabaseActionFactory();
            final DBI dbi = this.getDataSource();

            this.bind(IActionFactory.class).toInstance(actionFactory);
            this.bind(DBI.class).toInstance(dbi);
            this.bind(IDatabaseUpdater.class).to(DatabaseUpdater.class);
            this.bind(IRecordingDao.class).toInstance(dbi.onDemand(IRecordingDao.class));
            this.bind(IRecordingProjectDao.class).toInstance(dbi.onDemand(IRecordingProjectDao.class));

            Multibinder<IRequestHandler> requestHandlers = Multibinder.newSetBinder(binder(), IRequestHandler.class);
            requestHandlers.addBinding().to(IndexHandler.class);
        } catch (SQLException | PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     * @throws SQLException
     * @throws PropertyVetoException
     */
    protected DBI getDataSource() throws SQLException, PropertyVetoException {
        final ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.hsqldb.jdbc.JDBCDriver");
        cpds.setJdbcUrl("jdbc:hsqldb:file:db/testdb");
        cpds.setUser("SA");
        cpds.setPassword("");

        return new DBI(cpds);
    }
}