package com.oberasoftware.home.rules.evaluators.values;

import com.google.common.collect.Sets;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.StateItem;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.rules.api.values.ItemValue;
import com.oberasoftware.home.rules.evaluators.EvalException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class ItemValueEvaluator implements ValueEvaluator<ItemValue> {
    private static final Logger LOG = getLogger(ItemValueEvaluator.class);

    @Autowired
    private StateManager stateManager;

    @Override
    public Value eval(ItemValue input) {
        String itemId = input.getItemId();
        String label = input.getLabel();

        LOG.debug("Retrieving item: {} state value for label: {}", itemId, label);

        State state = stateManager.getState(itemId);
        if(state != null) {
            StateItem stateItem = state.getStateItem(label);

            if(stateItem != null) {
                return stateItem.getValue();
            }
        }

        throw new EvalException("Could not evaluate item: " + itemId + " label: " + label + " no values present");
    }

    @Override
    public Set<String> getDependentItems(ItemValue input) {
        return Sets.newHashSet(input.getItemId());
    }
}
