package com.oberasoftware.home.rules.api.values;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemValue itemValue = (ItemValue) o;

        if (!itemId.equals(itemValue.itemId)) return false;
        return label.equals(itemValue.label);

    }

    @Override
    public int hashCode() {
        int result = itemId.hashCode();
        result = 31 * result + label.hashCode();
        return result;
    }
}
