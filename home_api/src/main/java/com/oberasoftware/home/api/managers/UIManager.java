package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.home.api.model.storage.UIItem;

import java.util.List;

/**
 * @author renarj
 */
public interface UIManager {
    List<Container> getRootContainers();

    List<Container> getAllContainers();

    List<Container> getChildren(String containerId);

    Container getContainer(String containerId);

    List<UIItem> getItems(String containerId);

    void setWeight(String itemId, long weight);

    void setParentContainer(String itemId, String parentContainerId);

    void deleteContainer(String containerId);

    void deleteWidget(String itemId);

    UIItem store(UIItem item);

    Container store(Container container);
}
