package com.oberasoftware.home.api.model.storage;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface GroupItem extends VirtualItem {
    List<String> getDeviceIds();
}
