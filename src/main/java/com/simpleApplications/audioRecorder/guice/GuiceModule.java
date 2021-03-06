package com.simpleApplications.audioRecorder.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.simpleApplications.audioRecorder.actions.DatabaseActionFactory;
import com.simpleApplications.audioRecorder.actions.interfaces.IActionFactory;
import com.simpleApplications.audioRecorder.daos.DatabaseUpdater;
import com.simpleApplications.audioRecorder.daos.interfaces.IDatabaseUpdater;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingDao;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.handlers.IndexHandler;
import com.simpleApplications.audioRecorder.handlers.RecordingHandler;
import com.simpleApplications.audioRecorder.handlers.RecordingProjectHandler;
import com.simpleApplications.audioRecorder.handlers.StaticHandler;
import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import com.simpleApplications.audioRecorder.helper.FileHelper;
import com.simpleApplications.audioRecorder.helper.interfaces.IFileHelper;
import org.skife.jdbi.v2.DBI;
import ru.vyarus.guice.validator.ValidationModule;

import javax.validation.Validation;
import javax.validation.Validator;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Nico Moehring
 */
public class GuiceModule extends AbstractModule {

    protected Map<String, String> configuration;

    public GuiceModule(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        try {
            Names.bindProperties(binder(), this.configuration);
            this.bind(IFileHelper.class).to(FileHelper.class);

            final DBI dbi = this.getDataSource();

            this.bind(DBI.class).toInstance(dbi);
            this.bind(IDatabaseUpdater.class).to(DatabaseUpdater.class);
            this.bind(IRecordingDao.class).toInstance(dbi.onDemand(IRecordingDao.class));
            this.bind(IRecordingProjectDao.class).toInstance(dbi.onDemand(IRecordingProjectDao.class));

            Multibinder<IRequestHandler> requestHandlers = Multibinder.newSetBinder(binder(), IRequestHandler.class);
            requestHandlers.addBinding().to(StaticHandler.class);
            requestHandlers.addBinding().to(IndexHandler.class);
            requestHandlers.addBinding().to(RecordingHandler.class);
            requestHandlers.addBinding().to(RecordingProjectHandler.class);

            this.install(new ValidationModule());
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