package com.oberasoftware.home.rules.api.trigger;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DayTimeTrigger.class, name="daytime"),
        @JsonSubTypes.Type(value = DeviceTrigger.class, name="device")
})
public interface Trigger {

}
