package com.oberasoftware.home.rules.evaluators.values;

import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.StateItem;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.rules.api.ItemValue;
import com.oberasoftware.home.rules.evaluators.EvalException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class ItemValueEvaluator implements ValueEvaluator<ItemValue> {
    private static final Logger LOG = getLogger(ItemValueEvaluator.class);

    @Autowired
    private StateManager stateManager;

    private static final String MOVEMENT_LABEL = "movement";
    private static final String ONOFF_LABEL = "on-off";

    @Override
    public Value eval(ItemValue input) {
        String itemId = input.getItemId();
        String label = input.getLabel();
        label = checkMovementLabel(label);

        LOG.debug("Retrieving item: {} state value for label: {}", itemId, label);

        State state = stateManager.getState(itemId);
        if(state != null) {
            StateItem stateItem = state.getStateItem(label);
            return stateItem.getValue();
        }

        throw new EvalException("Could not evaluate item: " + itemId + " label: " + label + " no values present");
    }

    private String checkMovementLabel(String label) {
        if(label.equals(MOVEMENT_LABEL)) {
            return ONOFF_LABEL;
        }
        return label;
    }
}
