package com.oberasoftware.home.rules.api.general;

import com.oberasoftware.home.rules.api.Block;
import com.oberasoftware.home.rules.api.values.ItemValue;
import com.oberasoftware.home.rules.api.values.ResolvableValue;

/**
 * @author Renze de Vries
 */
public class SetState implements Block {

    private ItemValue itemValue;
    private ResolvableValue resolvableValue;

    public SetState(ItemValue itemValue, ResolvableValue resolvableValue) {
        this.itemValue = itemValue;
        this.resolvableValue = resolvableValue;
    }

    public SetState() {
    }

    public ItemValue getItemValue() {
        return itemValue;
    }

    public void setItemValue(ItemValue itemValue) {
        this.itemValue = itemValue;
    }

    public ResolvableValue getResolvableValue() {
        return resolvableValue;
    }

    public void setResolvableValue(ResolvableValue resolvableValue) {
        this.resolvableValue = resolvableValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetState that = (SetState) o;

        if (!itemValue.equals(that.itemValue)) return false;
        return resolvableValue.equals(that.resolvableValue);

    }

    @Override
    public int hashCode() {
        int result = itemValue.hashCode();
        result = 31 * result + resolvableValue.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SetStateAction{" +
                "itemValue=" + itemValue +
                ", resolvableValue=" + resolvableValue +
                '}';
    }
}

