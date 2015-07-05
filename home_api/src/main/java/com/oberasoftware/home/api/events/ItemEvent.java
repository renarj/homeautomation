package com.oberasoftware.home.api.events;

import com.oberasoftware.base.event.Event;

/**
 * @author Renze de Vries
 */
public interface ItemEvent extends Event {
    String getItemId();
}
