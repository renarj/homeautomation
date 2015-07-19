package com.oberasoftware.home.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.api.managers.RuleManager;
import com.oberasoftware.home.api.model.storage.RuleItem;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.core.model.storage.RuleItemImpl;
import com.oberasoftware.home.rules.RuleEngine;
import com.oberasoftware.home.rules.api.Rule;
import com.oberasoftware.home.rules.blockly.BlocklyParser;
import nl.renarj.core.utilities.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class RuleManagerImpl implements RuleManager {
    private static final Logger LOG = getLogger(RuleManagerImpl.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private HomeDAO homeDAO;

    @Autowired
    private CentralDatastore centralDatastore;

    @Autowired
    private BlocklyParser blocklyParser;

    @PostConstruct
    public void onStartup() {
        homeDAO.findRules().forEach(r -> {
            try {
                LOG.debug("Registering rule: {}", r);
                ruleEngine.addRule(toRule(r));
            } catch (HomeAutomationException e) {
                LOG.error("Could not load rule: " + r.toString(), e);
            }
        });
    }

    @Override
    public List<RuleItem> getRules() {
        return homeDAO.findRules();
    }

    @Override
    public List<RuleItem> getRules(String controllerId) {
        return homeDAO.findRules(controllerId);
    }

    @Override
    public RuleItem store(RuleItem ruleItem) throws HomeAutomationException {
        RuleItem storeItem = preProcessRule(ruleItem);

        centralDatastore.beginTransaction();
        try {
            return centralDatastore.store(storeItem);
        } catch (DataStoreException e) {
            LOG.error("Unable to store rule", e);
            throw new HomeAutomationException("Unable to store rule: " + ruleItem);
        } finally {
            centralDatastore.commitTransaction();

            ruleEngine.addRule(toRule(ruleItem));
        }
    }

    @Override
    public void delete(String ruleId) {
        centralDatastore.beginTransaction();
        try {
            centralDatastore.delete(RuleItemImpl.class, ruleId);
        } catch (DataStoreException e) {
            throw new RuntimeHomeAutomationException("Unable to delete rule: " + ruleId);
        } finally {
            centralDatastore.commitTransaction();
        }
    }

    @Override
    public RuleItem getRule(String ruleId) {
        Optional<RuleItemImpl> ruleItem = homeDAO.findItem(RuleItemImpl.class, ruleId);
        return ruleItem.get();
    }

    private RuleItem preProcessRule(RuleItem ruleItem) {
        String blocklyXML = ruleItem.getBlocklyXML();
        if(StringUtils.stringNotEmpty(blocklyXML)) {
            Rule rule = blocklyParser.toRule(blocklyXML);
            String json = toJson(rule);

            return new RuleItemImpl(ruleItem.getId(), ruleItem.getName(), ruleItem.getControllerId(), json, blocklyXML);
        }

        LOG.debug("Could not process blockly xml data, ignoring");
        return ruleItem;
    }

    private String toJson(Rule rule) {
        StringWriter writer = new StringWriter();
        try {
            OBJECT_MAPPER.writeValue(writer, rule);

            return writer.toString();
        } catch (IOException e) {
            LOG.error("Could not serialize rule to json", e);
            return null;
        }
    }

    private Rule toRule(RuleItem item) {
        try {
            return OBJECT_MAPPER.readValue(item.getRuleData(), Rule.class);
        } catch (IOException e) {
            LOG.error("Could not parse Rule: " + item.getRuleData());
            return null;
        }
    }
}
