package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.home.api.model.storage.MutableItem;
import com.oberasoftware.home.api.model.storage.Widget;

import java.util.List;

/**
 * @author renarj
 */
public interface UIManager {
    List<Container> getDashboardContainers(String dashboardId);

    List<Container> getAllContainers();

    List<Container> getChildren(String containerId);

    Container getContainer(String containerId);

    List<Widget> getItems(String containerId);

    void setWidgetProperty(String itemId, String property, String value);

    void setContainerProperty(String itemId, String property, String value);

    void setParentContainer(String itemId, String parentContainerId);

    void deleteContainer(String containerId);

    void deleteWidget(String itemId);

    <T extends MutableItem> T store(T item);
}
