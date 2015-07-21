package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.managers.RuleManager;
import com.oberasoftware.home.api.model.storage.RuleItem;
import com.oberasoftware.home.core.model.storage.RuleItemImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@RestController
@RequestMapping("/rules")
public class RulesController {
    private static final Logger LOG = getLogger(RulesController.class);

    @Autowired
    private RuleManager ruleManager;

    @RequestMapping(value = "/")
    public List<RuleItem> findAllRules() {
        return ruleManager.getRules();
    }

    @RequestMapping(value = "/controller({controllerId})")
    public List<RuleItem> findRules(@PathVariable String controllerId) {
        return ruleManager.getRules(controllerId);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public RuleItem createRule(@RequestBody RuleItemImpl item) throws HomeAutomationException {
        return ruleManager.store(item);
    }

    @RequestMapping(value = "({ruleId})", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public void deleteRule(@PathVariable String ruleId) {
        LOG.debug("Deleting Rule: {}", ruleId);
        ruleManager.delete(ruleId);
        LOG.debug("Deleted Rule");
    }
}
