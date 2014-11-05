package com.oberasoftware.home.zwave.converter;

import com.oberasoftware.home.api.MessageConverter;

import java.util.Set;

/**
 * @author renarj
 */
public interface ZWaveConverter<S, T> extends MessageConverter<S, T> {
    Set<String> getSupportedTypeNames();
}
