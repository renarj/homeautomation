package com.oberasoftware.home.web.model;

import com.oberasoftware.home.core.model.storage.ContainerImpl;
import com.oberasoftware.home.api.model.storage.UIItem;

import java.util.List;

/**
 * @author renarj
 */
public class WebContainer extends ContainerImpl {
    private final List<UIItem> items;

    public WebContainer(ContainerImpl container, List<UIItem> items) {
        super(container.getId(), container.getName(), container.getParentContainerId());
        this.items = items;
    }

    public List<UIItem> getItems() {
        return items;
    }
}
