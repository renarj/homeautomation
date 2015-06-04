package com.oberasoftware.home.api.commands.converters;

/**
 * @author renarj
 */
public interface CommandConverter<S, T> {
    T map(S source);
}
