package com.simpleApplications.audioRecorder.daos.interfaces;

/**
 * @author Nico Moehring
 */
public interface IDao<T> {
    public void save(T entity);

    public void delete(T entity);
}
