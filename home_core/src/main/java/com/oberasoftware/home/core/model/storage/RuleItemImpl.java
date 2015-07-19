package com.oberasoftware.home.core.model.storage;

import com.oberasoftware.home.api.model.storage.RuleItem;
import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

/**
 * @author Renze de Vries
 */
@JasDBEntity(bagName = "rules")
public class RuleItemImpl implements RuleItem {
    private String id;
    private String name;
    private String controllerId;
    private String ruleData;
    private String blocklyXML;

    public RuleItemImpl() {
    }

    public RuleItemImpl(String id, String name, String controllerId, String ruleData, String blocklyXML) {
        this.id = id;
        this.name = name;
        this.controllerId = controllerId;
        this.ruleData = ruleData;
        this.blocklyXML = blocklyXML;
    }

    @Override
    @Id
    @JasDBProperty
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    @JasDBProperty
    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    @Override
    @JasDBProperty
    public String getRuleData() {
        return ruleData;
    }

    public void setRuleData(String ruleData) {
        this.ruleData = ruleData;
    }

    @Override
    @JasDBProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JasDBProperty
    public String getBlocklyXML() {
        return blocklyXML;
    }

    public void setBlocklyXML(String blocklyXML) {
        this.blocklyXML = blocklyXML;
    }

    @Override
    public String toString() {
        return "RuleItemImpl{" +
                "blocklyXML='" + blocklyXML + '\'' +
                ", ruleData='" + ruleData + '\'' +
                ", controllerId='" + controllerId + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
