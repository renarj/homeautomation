package com.oberasoftware.home.zwave.core.utils;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

/**
 * @author renarj
 */
@FunctionalInterface
public interface EventSupplier<T> {
    T get() throws HomeAutomationException;
}
