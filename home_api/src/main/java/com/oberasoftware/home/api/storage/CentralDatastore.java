package com.oberasoftware.home.api.storage;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.storage.model.*;

import java.util.Optional;

/**
 * @author renarj
 */
public interface CentralDatastore {
    void beginTransaction();

    void commitTransaction();

    <T extends Item> T store(Item entity) throws DataStoreException;

    Container store(Container container) throws DataStoreException;

    <T extends Item> Optional<T> findItem(String id);

    Optional<ControllerItem> findController(String controllerId);

    <T extends Container> Optional<T> findContainer(String id);

    Optional<PluginItem> findPlugin(String controllerId, String pluginId);

    Optional<DeviceItem> findDevice(String controllerId, String pluginId, String deviceId);
}
