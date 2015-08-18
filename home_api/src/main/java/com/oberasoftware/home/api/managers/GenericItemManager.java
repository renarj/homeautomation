package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.storage.VirtualItem;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface GenericItemManager<T extends VirtualItem> {
    List<? extends T> getItems();

    List<? extends T> getItems(String controllerId);

    T getItem(String itemId);

    T store(T item);

    void delete(String itemId);
}
