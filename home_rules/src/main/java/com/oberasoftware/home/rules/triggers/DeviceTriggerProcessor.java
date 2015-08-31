package com.oberasoftware.home.rules.triggers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.events.DeviceEvent;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.rules.RuleEngine;
import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.trigger.DeviceTrigger;
import com.oberasoftware.home.rules.api.trigger.Trigger;
import com.oberasoftware.home.rules.evaluators.EvaluatorFactory;
import com.oberasoftware.home.rules.evaluators.blocks.BlockEvaluator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class DeviceTriggerProcessor implements TriggerProcessor, EventHandler {
    private static final Logger LOG = getLogger(DeviceTriggerProcessor.class);

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    private EvaluatorFactory evaluatorFactory;

    private Map<String, List<Rule>> itemMappedRules = new ConcurrentHashMap<>();

    @Override
    public void register(Trigger trigger, Rule rule) {
        if(trigger instanceof DeviceTrigger) {
            LOG.debug("Rule: {} has an item trigger, adding dependent items");
            Set<String> dependentItems = getDependentItems(rule.getBlock());
            LOG.debug("Adding dependent items: {} for rule: {}", dependentItems, rule);
            dependentItems.forEach(i -> addDependentItem(rule, i));
        }
    }

    @Override
    public void remove(Trigger trigger, Rule rule) {
        if(trigger instanceof DeviceTrigger) {
            Set<String> dependentItems = getDependentItems(rule.getBlock());
            dependentItems.forEach(d -> {
                List<Rule> deviceRules = itemMappedRules.get(d);
                deviceRules.remove(rule);
            });
        }
    }

    private Set<String> getDependentItems(Block block) {
        BlockEvaluator<Block> blockEvaluate = evaluatorFactory.getEvaluator(block);

        return blockEvaluate.getDependentItems(block);
    }

    private void addDependentItem(Rule rule, String itemId) {
        itemMappedRules.putIfAbsent(itemId, new CopyOnWriteArrayList<>());
        itemMappedRules.get(itemId).add(rule);
    }

    public void evaluateRules(String itemId) {
        LOG.debug("Evaluating rules for item state change: {}", itemId);

        List<Rule> itemRules = itemMappedRules.get(itemId);
        if(itemRules != null && !itemRules.isEmpty()) {
            LOG.debug("Rules: {} mapped to item: {}", itemRules.size(), itemId);

            itemRules.forEach(r -> ruleEngine.evalRule(r.getId()));
        } else {
            LOG.debug("No rules mapped for item: {}", itemId);
        }
    }

    @EventSubscribe
    public void receive(DeviceEvent event) throws Exception {
        LOG.debug("Received a device event: {}", event);
        Optional<DeviceItem> deviceItem = deviceManager.findDeviceItem(event.getControllerId(), event.getPluginId(), event.getDeviceId());
        if(deviceItem.isPresent()) {
            evaluateRules(deviceItem.get().getId());
        }
    }

}
