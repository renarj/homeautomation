package com.oberasoftware.home.rules.api.general;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.trigger.Trigger;

import java.util.List;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class Rule {

    private String id;
    private String name;
    private Block block;
    private List<Trigger> triggers;

    public Rule(String id, String name, Block block, List<Trigger> triggers) {
        this.id = id;
        this.name = name;
        this.block = block;
        this.triggers = triggers;
    }

    public Rule() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (!id.equals(rule.id)) return false;
        if (!name.equals(rule.name)) return false;
        if (!block.equals(rule.block)) return false;
        return triggers.equals(rule.triggers);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + block.hashCode();
        result = 31 * result + triggers.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", block=" + block +
                ", triggers=" + triggers +
                '}';
    }
}
