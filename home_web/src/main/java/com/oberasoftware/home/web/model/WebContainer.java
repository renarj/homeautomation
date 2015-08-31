package com.oberasoftware.home.web.model;

import com.oberasoftware.home.api.model.storage.Widget;
import com.oberasoftware.home.core.model.storage.ContainerImpl;

import java.util.List;

/**
 * @author renarj
 */
public class WebContainer extends ContainerImpl {
    private final List<Widget> items;

    public WebContainer(ContainerImpl container, List<Widget> items) {
        super(container.getId(), container.getName(), container.getDashboardId(), container.getParentContainerId(), container.getProperties());
        this.items = items;
    }

    public List<Widget> getItems() {
        return items;
    }
}
