package com.oberasoftware.home.rules.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oberasoftware.home.rules.api.general.SetState;
import com.oberasoftware.home.rules.api.general.SwitchItem;
import com.oberasoftware.home.rules.api.logic.IfBlock;
import com.oberasoftware.home.rules.api.logic.IfBranch;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IfBlock.class, name="ifBlock"),
        @JsonSubTypes.Type(value = IfBranch.class, name="ifBranch"),
        @JsonSubTypes.Type(value = SwitchItem.class, name="switch"),
        @JsonSubTypes.Type(value = SetState.class, name="setState")
})
public interface Block {

}
