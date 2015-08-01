package com.oberasoftware.home.rules.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oberasoftware.home.rules.api.logic.CompareCondition;
import com.oberasoftware.home.rules.api.logic.LogicCondition;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CompareCondition.class, name="compare"),
        @JsonSubTypes.Type(value = LogicCondition.class, name="logic")
})
public interface Condition {
}
