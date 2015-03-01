package com.oberasoftware.home.storage.jasdb.mapping;

import nl.renarj.jasdb.api.SimpleEntity;

/**
 * @author renarj
 */
public interface EntityMapper<T> {
    SimpleEntity mapFrom(T input);

    T mapTo(SimpleEntity entity);
}
