package com.oberasoftware.home.api;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

import java.util.Set;

/**
 * @author renarj
 */
public interface MessageConverter<S, T> {
    T convert(S source) throws HomeAutomationException;
}
