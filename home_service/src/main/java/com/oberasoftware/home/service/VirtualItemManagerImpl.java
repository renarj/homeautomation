package com.oberasoftware.home.service;

import com.oberasoftware.home.api.managers.VirtualItemManager;
import com.oberasoftware.home.api.model.storage.VirtualItem;
import com.oberasoftware.home.core.model.storage.VirtualItemImpl;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class VirtualItemManagerImpl extends GenericItemManagerImpl<VirtualItem> implements VirtualItemManager {
    @Override
    protected Class<? extends VirtualItem> getType() {
        return VirtualItemImpl.class;
    }
}
