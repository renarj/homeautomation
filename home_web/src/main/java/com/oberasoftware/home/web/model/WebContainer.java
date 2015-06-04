package com.oberasoftware.home.web.model;

import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.api.storage.model.UIItem;

import java.util.List;

/**
 * @author renarj
 */
public class WebContainer extends Container {
    private final List<UIItem> items;

    public WebContainer(Container container, List<UIItem> items) {
        super(container.getId(), container.getName(), container.getParentContainerId());
        this.items = items;
    }

    public List<UIItem> getItems() {
        return items;
    }
}
