package com.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class IValue extends Value<Integer>{
    protected Integer min, max;

    public IValue(String name, Integer defaultValue, Integer min, Integer max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public IValue visibility(Predicate<Integer> predicate) {
        return (IValue) super.visibility(predicate);
    }

    public Integer getMin(){
        return min;
    }

    public Integer getMax() {
        return max;
    }
}