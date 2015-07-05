package com.oberasoftware.home.api.events;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface GroupEvent extends ItemEvent {
    List<String> getItemIds();
}
