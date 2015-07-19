package com.oberasoftware.home.rules.api;

/**
 * @author Renze de Vries
 */
public class ItemValue implements ResolvableValue {

    private String itemId;
    private String label;

    public ItemValue(String itemId, String label) {
        this.itemId = itemId;
        this.label = label;
    }

    public ItemValue() {
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ItemValueImpl{" +
                "itemId='" + itemId + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
