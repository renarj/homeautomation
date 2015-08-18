package com.oberasoftware.home.api.storage;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.home.api.model.storage.HomeEntity;

/**
 * @author renarj
 */
public interface CentralDatastore {
    void beginTransaction();

    void commitTransaction();

    void delete(Class<?> type, String id) throws DataStoreException;

    <T extends HomeEntity> T store(HomeEntity entity) throws DataStoreException;

    Container store(Container container) throws DataStoreException;

    HomeDAO getDAO();
}
