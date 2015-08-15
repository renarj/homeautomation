package com.oberasoftware.home.rules.api.values;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StaticValue.class, name="static"),
        @JsonSubTypes.Type(value = ItemValue.class, name="device"),
        @JsonSubTypes.Type(value = MathValue.class, name="math")
})
public interface ResolvableValue {
}
