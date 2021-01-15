package com.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class FValue extends Value<Float>{
    protected Float min, max;

    public FValue(String name, Float defaultValue, Float min, Float max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public FValue visibility(Predicate<Float> predicate) {
        return (FValue) super.visibility(predicate);
    }

    public Float getMin() {
        return min;
    }

    public Float getMax() { return max; }
}
