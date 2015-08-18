package com.oberasoftware.home.api.model.storage;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface RuleItem extends Item {
    String getName();

    String getControllerId();

    String getRule();

    Map<String, String> getProperties();
}
