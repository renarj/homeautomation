package com.oberasoftware.home.api.storage;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.api.storage.model.Item;

/**
 * @author renarj
 */
public interface CentralDatastore {
    void beginTransaction();

    void commitTransaction();

    <T extends Item> T store(Item entity) throws DataStoreException;

    Container store(Container container) throws DataStoreException;

    HomeDAO getDAO();
}
