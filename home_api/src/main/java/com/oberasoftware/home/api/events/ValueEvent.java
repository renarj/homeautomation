package com.oberasoftware.home.api.events;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.home.api.types.Value;

/**
 * @author Renze de Vries
 */
public interface ValueEvent extends Event {
    String getLabel();

    Value getValue();
}
