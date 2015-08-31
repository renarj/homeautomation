package com.oberasoftware.home.api.model.storage;

/**
 * @author Renze de Vries
 */
public interface RuleItem extends Item {
    String getName();

    String getControllerId();

    String getRule();
}
