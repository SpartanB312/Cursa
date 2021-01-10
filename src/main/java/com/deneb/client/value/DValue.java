package com.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class DValue extends Value<Double> {

    protected Double min, max;

    public DValue(String name, Double defaultValue, Double min, Double max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public DValue visibility(Predicate<Double> predicate) {
        return (DValue) super.visibility(predicate);
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }
}
